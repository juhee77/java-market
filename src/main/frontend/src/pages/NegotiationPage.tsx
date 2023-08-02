import React, {useContext} from 'react';
import {Item, Negotiation} from 'type/types';
import AuthContext from 'store/auth-context';
import {updateProposal} from 'store/auth-action';

type Props = {
    negotiations: Negotiation[] | undefined;
    item: Item | undefined;
}


const ItemDetails: React.FC<Props> = (props) => {
    const {negotiations, item} = props;
    const authCtx = useContext(AuthContext);

    if (!negotiations || !item) {
        return <p>Loading...</p>;
    }

    const handleStatusChange = (negotiationId: number, newStatus: string) => {
        console.log(negotiationId, newStatus)
        const token = authCtx.token;
        updateProposal(token, item.id, negotiationId, newStatus).then((result) => {
                if (result != null) {
                    console.log("아이템 상태 업데이트");
                    window.location.reload();
                }
            }
        );
    };

    return (
        <div className='mt-4 '>
            <h3>입력된 제안들</h3>
            {negotiations.length > 0 ? (
                <ul className="list-group container mt-4 li-hover">
                    {negotiations.map((negotiation) => (
                        <li className="list-group-item list-group-item-action" key={negotiation.id}>
                            <p>{negotiation.proposalName}님이 {negotiation.suggestedPrice}원을 제안하셨습니다.</p>
                            <p> 상태 : {negotiation.status}</p>

                            {(
                                <div>
                                    {
                                        negotiation.proposalName == authCtx.userObj.nickname && negotiation.status == '수락' &&
                                        <button className='btn btn-outline-primary'
                                                onClick={() => handleStatusChange(negotiation.id, '확정')}>
                                            확정
                                        </button>
                                    }
                                    {
                                        item.seller == authCtx.userObj.nickname && negotiation.status != '확정' &&
                                        <button className='btn btn-outline-primary'
                                                onClick={() => handleStatusChange(negotiation.id, '수락')}>
                                            수락
                                        </button>
                                    }
                                    {item.seller == authCtx.userObj.nickname && negotiation.status != '확정' &&
                                        <button className='btn btn-outline-primary'
                                                onClick={() => handleStatusChange(negotiation.id, '거절')}>
                                            거절
                                        </button>
                                    }
                                </div>
                            )}

                        </li>
                    ))}

                </ul>
            ) : (
                <p>No proposal yet.</p>
            )}

        </div>
    );
};

export default ItemDetails;

function createTokenHeader(token: string): ((this: any, key: string, value: any) => any) | undefined {
    throw new Error('Function not implemented.');
}

