import React, {useContext, useEffect, useRef, useState} from "react";
import {Params, useNavigate, useParams} from "react-router-dom";
import * as Stomp from "@stomp/stompjs";
import AuthContext from "store/auth-context";
import {ChatMessage, ChatRoomDetail} from "type/types";
import {getEachChatroomHandler} from "store/auth-action";
import './ChatPage.css'

type props = {
    roomId: string;
};

const ChatPage: React.FC<props> = (porps) => {
    const client = useRef<Stomp.Client | null>(null);
    const [messages, setMessages] = useState<ChatMessage[]>([]);
    const [chatRoom, setChatRooms] = useState<ChatRoomDetail>();
    const {roomId} = useParams<Params>();

    const [message, setMessage] = useState<string>("");
    const authCtx = useContext(AuthContext);
    const messageContainerRef = useRef<HTMLDivElement>(null);
    const [enter, setEnter] = useState<boolean>(false);
    const headers = {Authorization: "Bearer " + authCtx.token};
    const navigate = useNavigate(); // useNavigate hook 사용
    const token = authCtx.token;

    function findRoomDetail() {
        getEachChatroomHandler(authCtx.token, roomId).then((response) => {
            if (response != null) {
                setChatRooms(response.data)
                console.log(response.data)
            }
        });
    }

    useEffect(() => {
        console.log("testing" + roomId + " ");
        findRoomDetail(); //먼저 방의 자세한 정보를 받아온다. 
        connect(); //접속한다.
    }, []);

    const connect = async () => {
        client.current = new Stomp.Client({
            brokerURL: "ws://localhost:8080/ws",
            connectHeaders: headers,
            onConnect: async () => {
                enterRoom();
                subscribe();
                console.log("success");
            },
        });
        // 접속한다.
        client.current?.activate();
    };

    const destination = "/user/" + authCtx.userObj.nickname + "/sub/chat/enter/" + roomId;
    const subscribe = async () => {
        console.log(destination);
        client.current?.subscribe(destination, (body) => {
            const chatlist = JSON.parse(body.body);
            console.log("과거기록 들어옴");

            setMessages(
                chatlist.map(
                    (chat: { messageType: any; roomId: any; sender: any; message: any; time: any }) => ({
                        messageType: chat.messageType,
                        roomId: chat.roomId,
                        sender: chat.sender,
                        message: chat.message,
                        time: chat.time,
                    })
                )
            );
        });

        client.current?.subscribe("/sub/chat/" + roomId, (body) => {
            const parsed_body = JSON.parse(body.body);
            const {messageType, roomId, sender, message, time} = parsed_body; // 파싱된 정보를 추출

            setMessages((_chat_list) => [
                ..._chat_list, //이전에 있던 데이터들
                {
                    messageType: messageType,
                    roomId: roomId,
                    sender: sender,
                    message: message,
                    time: time,
                }
            ]);
            findRoomDetail();
        });
    };

    const disconnect = () => {
        console.log("subDisconnect");
        outRoom();
        client.current?.deactivate();
    };

    const handleMessageChange = (event: React.ChangeEvent<HTMLInputElement>) => {
        setMessage(event.target.value);
    };

    const enterRoom = () => {
        console.log("ENTER")
        console.log("??" + roomId, authCtx.userObj.username)
        if (!enter) {
            //처음 접속 한 경우에만
            client.current?.publish({
                destination: "/pub/chat/enter",
                body: JSON.stringify({
                    roomId: roomId ? roomId : "ERROR",
                    writer: authCtx.userObj.username,
                    message: message,
                }),
            });

            setEnter(true);
        }
    };

    const outRoom = () => {
        setEnter(false);
    };

    const handleSendMessage = () => {
        if (!authCtx.token) {
            setMessage("token이 종료되어서 메인으로 이동합니다.");
            navigate('/')
            return;
        }

        client.current?.publish({
            destination: "/pub/chat/send",
            body: JSON.stringify({
                roomId: roomId ? roomId : "ERROR",
                writer: authCtx.userObj.nickname,
                message: message,
            }),
        });
        setMessage("");
    };

    const handleOutChatRoom = () => {
        disconnect();
        navigate("/chatroomlist-view");
    };

    useEffect(() => {
        if (messageContainerRef.current) {
            messageContainerRef.current.scrollTop =
                messageContainerRef.current.scrollHeight;
        }
    }, [messages]);

    return (
        <div className="chat-page-container">
            <div className="chat-header">
                <h1>{chatRoom?.itemName}에 대한 {chatRoom?.roomName} 님의 제안입니다. </h1>
                <div> 판매자 : {chatRoom?.itemSeller}</div>
                <div> 제품 설명 : {chatRoom?.itemDescription}</div>
                {!chatRoom?.active && <div className="chat-alter"> 아직 판매자가 해당 채팅을 읽도록 수락한 상태가 아닙니다.</div>}

            </div>
            <div className="chat-messages-container" ref={messageContainerRef}>
                {messages.map((msg, index) => (
                    <div key={index}
                         className={`chat-bubble ${msg.sender === authCtx.userObj.nickname ? "mine" : "theirs"}`}>
                        <div
                            className={`chat-message-writer ${msg.sender === authCtx.userObj.nickname ? "other" : "me"}`}>
                            {msg.sender}
                        </div>
                        <div className="chat-bubble-message">{msg.message}</div>
                        <div className="chat-bubble-message">{msg.time}</div>
                    </div>
                ))}
            </div>
            <div className="chat-input-container">
                <input
                    type="text"
                    placeholder="Type your message"
                    value={message}
                    onChange={handleMessageChange}
                />
                <button onClick={handleSendMessage}>Send</button>
            </div>
            <button onClick={handleOutChatRoom} className="btn btn-outline-secondary">채팅방 나가기</button>
        </div>
    );
};

export default ChatPage;
