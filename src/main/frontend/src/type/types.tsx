import { type } from "os";

export type Item = {
    id:number;
    seller:string;
    title:string;
    description:string;
    minPriceWanted:number;
    status:string;
    imageUrl : string;
    comments : Comment[];
}

export type Comment = {
    id:number;
    content:string;
    reply:string;
    writer:string;
}

export type Negotiation = {
    id :number;
    suggestedPrice : number;
    status : string;
    proposalName : string;
}
