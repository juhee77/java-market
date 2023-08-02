import { useContext, useRef, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import AuthContext from '../../store/auth-context';
import { POST } from 'store/fetch-auth-action';
import { ChatRoom } from 'type/types';

type Props = {
    chatRooms: ChatRoom[];
};

const ChatRoomListForm: React.FC<Props> = ({ chatRooms }) => {
    const authCtx = useContext(AuthContext);
    const token = authCtx.token;
    const navigate = useNavigate(); 

    const handleChatRoomClick = (chatRoom: ChatRoom) => {
        console.log(chatRoom.id + '을 클릭함');
        navigate(`/chatroom-view/${chatRoom.id}`);
    };

    return (
        <div className="chat-room-list-container">
            <h1>Chat Room List</h1>
            <table className="table table-hover">
                <thead>
                    <tr>
                        <th>순서</th>
                        <th>방이름</th>
                        <th>판매하는 아이템</th>
                        <th>아이템 판매자 이름</th>
                        <th>아이템 판매자의 채팅 수락여부</th>
                    </tr>
                </thead>
                <tbody>
                    {chatRooms.map((chatRoom, index) => (
                        <tr key={chatRoom.id} onClick={() => handleChatRoomClick(chatRoom)}>
                            <td>{index + 1}</td>
                            <td>{chatRoom.roomName}</td>
                            <td>{chatRoom.itemName}</td>
                            <td>{chatRoom.itemSeller}</td>
                            <td>{chatRoom.isActive}</td>
                        </tr>
                    ))}
                </tbody>
            </table>
        </div>
    );
};

export default ChatRoomListForm;
