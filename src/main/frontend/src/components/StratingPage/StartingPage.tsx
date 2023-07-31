import axios from 'axios';
import { useEffect, useState } from 'react';

interface Item {
    id: number;
    title: string;
    seller: string;
    minPriceWanted : number;
    status : string;
}
interface PagingResponse {
    content: Item[];
  }
const StartingPage = () => {

    const [posts, setPosts] = useState<Item[]>([]);
    const [loading, setLoading] = useState<boolean>(false);

    useEffect(() => {
        const fetchData = async () => {
            try{
            setLoading(true);
            const response = await axios.get<PagingResponse>('items');
            setPosts(response.data.content);
            setLoading(false);
            }catch(error){
                console.error("Error fetching data:", error);
                setLoading(false);
            }
        };
        fetchData();
    }, []);

    console.log(posts);

    return (
        <div className='form-wrapper'>
            {loading ? (
                <div>Loading...</div>
            ) : (
                <table className="table">
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
                        {posts.map((Item, index) => (
                            <tr key={Item.id}>
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