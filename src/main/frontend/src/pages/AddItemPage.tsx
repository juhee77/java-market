import React, { useContext, useRef, useState } from "react";
import { useNavigate } from "react-router-dom";
import { addItemHandler } from "store/auth-action";
import AuthContext from "store/auth-context";
const AddItemPage = () => {
    let navigate = useNavigate();
    const authCtx = useContext(AuthContext);
    const titleInputRef = useRef<HTMLInputElement>(null);
    const descriptionInputRef = useRef<HTMLInputElement>(null);
    const minPriceWantedInputRef = useRef<HTMLInputElement>(null);
    const itemImageRef = useRef<HTMLInputElement>(null);

    const submitHandler = async (event: React.FormEvent) => {
        event.preventDefault();

        const title = titleInputRef.current!.value;
        const description = descriptionInputRef.current!.value;
        const minPriceWanted = minPriceWantedInputRef.current!.value;
        let file = null

        if (itemImageRef.current?.files) {
            file = itemImageRef.current.files[0] || null;
        }

        addItemHandler(authCtx.token, title, description, minPriceWanted, file).then((result) => {
            if (result !== null) {
                navigate("/", { replace: true });
            } else {
                navigate("/item-add-view", { replace: true });
            }
        });
    }

    return (
        <section>
            <h1>아이템 생성</h1>
            <form onSubmit={submitHandler} className="form-horizontal">
                <div className="form-group">
                    <label htmlFor='title' className="col-sm-2 control-label">제목</label>
                    <input type='title' id='title' className="form-control" required ref={titleInputRef} />
                </div>
                <div className="form-group">
                    <label htmlFor="description" className="col-sm-2 control-label">설명</label>
                    <input type='description' id='description' className="form-control" required ref={descriptionInputRef} />
                </div>
                <div className="form-group">
                    <label htmlFor="minPriced" className="col-sm-2 control-label">최소가격</label>
                    <input type='minPriced' id='minPriced' className="form-control" required ref={minPriceWantedInputRef} />
                </div>
                <div className="form-group">
                    <label htmlFor="image" className="col-md-2 control-label">아이템 이지지 업로드</label>
                    <input type="file" accept="image/jpg, image/png, image/jpeg" id="image" className="form-controle" ref={itemImageRef} />
                </div>

                <div>
                    <button type='submit' className="btn btn-primary btn"> Submit</button>
                </div>
            </form>
        </section>
    );
}

export default AddItemPage;