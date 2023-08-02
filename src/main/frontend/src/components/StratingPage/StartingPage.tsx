import axios from 'axios';
import {useContext, useEffect, useState} from 'react';
import {useNavigate} from 'react-router-dom';
import AuthContext from 'store/auth-context';

interface Item {
    id: number;
    title: string;
    seller: string;
    minPriceWanted: number;
    status: string;
}

interface PagingResponse {
    content: Item[];
}

const StartingPage = () => {
    let navigate = useNavigate();
    const authCtx = useContext(AuthContext)
    const [items, setItems] = useState<Item[]>([]);
    const [loading, setLoading] = useState<boolean>(false);

    useEffect(() => {
        const fetchData = async () => {
            try {
                setLoading(true);
                const response = await axios.get<PagingResponse>('items');
                setItems(response.data.content);
                setLoading(false);
            } catch (error) {
                console.error("Error fetching data:", error);
                setLoading(false);
            }
        };
        fetchData();
    }, []);


    const handleItemClick = (item: Item) => {
        console.log('connect chat room');
        if (!authCtx.token) {
            console.log('로그인 후 게속해주세요 ');
            return;
        }
        console.log(item.id + '을 클릭함');
        navigate(`/item-view/${item.id}`);
    };

    return (
        <div className='form-wrapper'>
            {loading ? (
                <div>Loading...</div>
            ) : (
                <table className="table table-hover">
                    <thead>
                    <tr>
                        <th>번호</th>
                        <th>제목</th>
                        <th>판매자</th>
                        <th>가격</th>
                        <th>상태</th>
                    </tr>
                    </thead>
                    <tbody>
                    {items.map((Item, index) => (
                        <tr key={Item.id} onClick={() => handleItemClick(Item)}>
                            <td>{index + 1}</td>
                            <td>{Item.title}</td>
                            <td>{Item.seller}</td>
                            <td>{Item.minPriceWanted}</td>
                            <td>{Item.status}</td>
                        </tr>
                    ))}
                    </tbody>
                </table>
            )}
        </div>
    );
}

export default StartingPage;