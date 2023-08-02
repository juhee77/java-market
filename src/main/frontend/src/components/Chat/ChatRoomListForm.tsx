import {useContext} from 'react';
import {useNavigate} from 'react-router-dom';
import AuthContext from '../../store/auth-context';
import {ChatRoom} from 'type/types';

type Props = {
    chatRooms: ChatRoom[];
};

const ChatRoomListForm: React.FC<Props> = ({chatRooms}) => {
    const authCtx = useContext(AuthContext);
    const navigate = useNavigate();
    console.log(chatRooms)

    const handleChatRoomClick = (chatRoom: ChatRoom) => {
        console.log(chatRoom.id + '을 클릭함' + chatRoom.active);
        navigate(`/chatroom-view/${chatRoom.id}`);
    };

    return (
        <div className="container mt-4">
            <div className="container mt-4 mb-4">
                <h1>수락 가능한 채팅방(제안들)</h1>

                <ul className="list-group container mt-4 li-hover">
                    {chatRooms.filter(chatRoom =>
                        !chatRoom.active && chatRoom.itemSeller === authCtx.userObj.username
                    ).length === 0 ? (
                        <li className="list-group-item">없음</li>
                    ) : (
                        chatRooms.filter(chatRoom =>
                            !chatRoom.active && chatRoom.itemSeller === authCtx.userObj.username
                        ).map((filteredChatRoom, index) => (
                            <li
                                key={filteredChatRoom.id}
                                className="list-group-item list-group-item-action"
                                onClick={() => handleChatRoomClick(filteredChatRoom)}
                            >
                                방이름: {filteredChatRoom.roomName}, 판매하는 아이템: {filteredChatRoom.itemName}
                            </li>
                        ))
                    )}
                </ul>
            </div>


            <h1>Chat Room List</h1>
            <table className="table table-hover ">
                <thead>
                <tr>
                    <th>순서</th>
                    <th>방이름</th>
                    <th>판매하는 아이템</th>
                    <th>아이템 판매자 이름</th>
                </tr>
                </thead>
                <tbody>
                {chatRooms.map((chatRoom, index) => (
                    ((chatRoom.active && chatRoom.itemSeller == authCtx.userObj.username) || (chatRoom.itemSeller != authCtx.userObj.username)) &&
                    <tr key={chatRoom.id} onClick={() => handleChatRoomClick(chatRoom)}>
                        <td>{index + 1}</td>
                        <td>{chatRoom.roomName}</td>
                        <td>{chatRoom.itemName}</td>
                        <td>{chatRoom.itemSeller}</td>
                    </tr>
                ))}
                </tbody>
            </table>
        </div>
    );
};

export default ChatRoomListForm;
