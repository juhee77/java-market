import ItemDetails from "components/Item/ItemDetails";
import NegotiationPage from "pages/NegotiationPage";
import React, {useContext, useEffect, useState} from "react";
import {useParams} from "react-router-dom";
import {getEachItemHandler, getItemProposalHandler} from "store/auth-action";
import AuthContext from "store/auth-context";
import {Item, Negotiation} from "type/types";


type props = {
    itemId: string;
};

export interface ItemPagingResponse {
    content: Negotiation[];
}

const ItemPage: React.FC<props> = () => {
    const authCtx = useContext(AuthContext)
    const token = authCtx.token;
    const [loading, setLoading] = useState<boolean>(false);
    const [proposalLoging, setProposalLoding] = useState<boolean>(false);
    const [item, setItem] = useState<Item>();
    const [negotiations, setNegotiation] = useState<Negotiation[]>();
    const {itemId} = useParams<string>();


    useEffect(() => {
        setLoading(true);
        getEachItemHandler(token, itemId).then((response) => {
            if (response !== null) {
                console.log('item얻어옴');
                const itemData = response.data;
                setItem(itemData);
            }
        });
        setLoading(false);
    }, []);

    useEffect(() => {
        setProposalLoding(true);
        getItemProposalHandler(token, itemId).then((response) => {
            if (response !== null) {
                console.log('proposal 얻어옴');
                const negotiations: ItemPagingResponse = response.data;
                setNegotiation(negotiations.content);
            }
        });
        setProposalLoding(false);
    }, []);

    return (
        <div className="container mt-4">
            <ItemDetails item={item}/>
            <NegotiationPage negotiations={negotiations} item={item}/>
        </div>
    );
};

export default ItemPage;
