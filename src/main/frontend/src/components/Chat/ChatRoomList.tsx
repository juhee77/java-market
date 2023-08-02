import React, { useContext, useEffect, useState } from 'react';
import AuthContext from 'store/auth-context';
import ChatRoomListForm from './ChatRoomListForm';
import { ChatRoom } from 'type/types';
import { getChatroomHandler } from 'store/auth-action';
import { response } from 'express';


const ChatRoomList: React.FC = () => {
    const authCtx = useContext(AuthContext);
    const [chatRooms, setChatRooms] = useState<ChatRoom[]>([]);
    const [loading, setLoading] = useState<boolean>(false);

    const [error, setError] = useState<string>('');

    useEffect(() => {
        setLoading(true);

        getChatroomHandler(authCtx.token).then((response) => { 
            if(response!=null){
                console.log('채팅방 얻어옴');
                setChatRooms(response.data)
            }
        })

        setLoading(false);
    }, []);

    if (error) {
        return <div>{error}</div>;
    }

    return <ChatRoomListForm chatRooms={chatRooms} />;
};

export default ChatRoomList;
