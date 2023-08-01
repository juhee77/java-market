import React, { FormEvent, useContext, useEffect, useRef, useState } from 'react';
import { Item } from 'type/types';
import "./ItemDetails.css"
import AuthContext from 'store/auth-context';
import { Params, useNavigate, useParams } from 'react-router-dom';
import { POST } from 'store/fetch-auth-action';
import { addComment, addNegotiation, replyComment } from 'store/auth-action';

type Props = {
    item: Item | undefined;
}

const ItemDetails: React.FC<Props> = ({ item }) => {
    const commentInputRef = useRef<HTMLInputElement>(null);
    const replyInpuputRef = useRef<HTMLInputElement>(null);
    const negotiationInputRef = useRef<HTMLInputElement>(null);
    const authCtx = useContext(AuthContext);
    const token = authCtx.token;

    const [expandedCommentIds, setExpandedCommentIds] = useState<number[]>([]);

    if (!item) {
        return <p>Loading...</p>; // 예외 처리: item이 undefined일 경우 로딩 메시지를 보여줍니다.
    }

    const toggleReplyForm = (commentId: number) => {
        setExpandedCommentIds((prevExpandedCommentIds) =>
            prevExpandedCommentIds.includes(commentId)
                ? prevExpandedCommentIds.filter((id) => id !== commentId)
                : [...prevExpandedCommentIds, commentId]
        );
    };

    const submitCommentHandler = async (event: React.FormEvent) => {
        event.preventDefault();
        const comment = commentInputRef.current!.value;
        console.log(comment);
        addComment(token, item.id, comment).then((result) => {
            if (result != null) {
                console.log("댓글 생성");
                window.location.reload();
            }
        });
    }


    const submitReplyHandler = async (event: React.FormEvent, commentId: number) => {
        event.preventDefault();
        const reply = replyInpuputRef.current!.value;

        replyComment(token, item.id, commentId, reply).then((result) => {
            if (result != null) {
                console.log("대댓글 생성");
                window.location.reload();
            }
        });
    }


    const submitNegotiationHandler = async (event: React.FormEvent) => {
        event.preventDefault();
        const suggestedPrice = negotiationInputRef.current!.value;

        addNegotiation(token, item.id, suggestedPrice).then((result) => {
            if (result != null) {
                console.log("제안 생성");
                window.location.reload();
            }
        });
    }



    return (
        <div>
            <h2>판매 이름 : {item.title}</h2>
            <div>
                {item.imageUrl != null &&
                    <img
                        src={item.imageUrl}
                        alt="Image"
                        style={{ maxWidth: '100%', height: 'auto' }}
                    />
                }
            </div>
            <p>설명 : {item.description}</p>
            <p>가격: {item.minPriceWanted} 원</p>
            <h3>입력된 댓글들</h3>
            {item.comments.length > 0 ? (
                <ul>
                    {item.comments.map((comment) => (
                        <li key={comment.id}>
                            <p>{comment.writer}의 댓글</p>
                            <p>{comment.content}</p>
                            <p className="reply">{comment.reply ? comment.reply : ''}</p>



                            {expandedCommentIds.includes(comment.id) && (
                                <form onSubmit={(event) => submitReplyHandler(event, comment.id)} className="form-horizontal">
                                    <div className="form-group">
                                        <label htmlFor="reply" className="col-sm-2 control-label">
                                            대댓글
                                        </label>
                                        <input type="reply" id="reply" className="form-control" required ref={replyInpuputRef} />
                                    </div>
                                    <button type="submit" className="btn btn-primary btn">
                                        대댓글 남기기
                                    </button>
                                </form>
                            )}

                            <button type="button" onClick={() => toggleReplyForm(comment.id)} className="btn btn-primary btn">
                                {expandedCommentIds.includes(comment.id) ? '대댓글 닫기' : '대댓글 작성'}
                            </button>
                        </li>
                    ))}
                </ul>
            ) : (
                <p>No comments yet.</p>
            )}

            <h3>댓글 달기</h3>
            <form onSubmit={submitCommentHandler} className="form-horizontal">
                <div className="form-group">
                    <label htmlFor="comment" className="col-sm-2 control-label">
                        댓글
                    </label>
                    <input type="text" id="comment" className="form-control" required ref={commentInputRef} />
                </div>
                <button type="submit" className="btn btn-primary btn">
                    댓글 남기기
                </button>
            </form>


            <h3>제안 보내기</h3>
            <form onSubmit={submitNegotiationHandler} className="form-horizontal">
                <div className="form-group">
                    <label htmlFor="comment" className="col-sm-2 control-label">
                        제안 가격
                    </label>
                    <input type="number" id="negotiation" className="form-control" required ref={negotiationInputRef} />
                </div>
                <button type="submit" className="btn btn-primary btn">
                    제안 보내기
                </button>
            </form>
        </div>
    );
};

export default ItemDetails;

