import ItemDetails from "components/Item/ItemDetails";
import NegotiationPage from "components/NegotiationPage";
import React, { useContext, useEffect, useRef, useState } from "react";
import { Params, useNavigate, useParams } from "react-router-dom";
import { getEachItemHandler, getItemProposalHandler } from "store/auth-action";
import AuthContext from "store/auth-context";
import { Item, Negotiation } from "type/types";


type props = {
    itemId: string;
};

export interface ItemPagingResponse {
    content: Negotiation[];
}

const ItemPage: React.FC<props> = () => {
    const authCtx = useContext(AuthContext)
    const token = authCtx.token;
    const [item, setItem] = useState<Item>();
    const [negotiations, setNegotiation] = useState<Negotiation[]>();
    const { itemId } = useParams<string>();

    console.log(itemId)

    if (itemId == null) {
        return <p>Loading...</p>;
    }

    if (item == null) {
        getEachItemHandler(token, itemId).then((response) => {
            if (response !== null) {
                console.log('item얻어옴');
                const itemData = response.data;
                setItem(itemData);
            }
        });
    }
    if (negotiations == null) {
        getItemProposalHandler(token, itemId).then((response) => {
            if (response !== null) {
                console.log('proposal 얻어옴');
                const negotiations : ItemPagingResponse = response.data;
                setNegotiation(negotiations.content);
            }
        });
    }



    return (
        <div className="container mt-4">
            <h1>이번 아이템은!?</h1>
            <ItemDetails item={item} />
            <h1>제안들</h1>
            <NegotiationPage negotiations={negotiations} item={item} />
        </div>
    );
};

export default ItemPage;
