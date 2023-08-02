export type Item = {
    id: number;
    seller: string;
    title: string;
    description: string;
    minPriceWanted: number;
    status: string;
    imageUrl: string;
    comments: Comment[];
}

export type Comment = {
    id: number;
    content: string;
    reply: string;
    writer: string;
}

export type Negotiation = {
    id: number;
    suggestedPrice: number;
    status: string;
    proposalName: string;
}

export type ChatRoom = {
    id: number,
    roomName: string,
    itemId: number,
    itemName: string,
    itemSeller: string,
    active: boolean
}

export type ChatRoomDetail = {
    id: number,
    roomName: string,
    itemId: number,
    itemName: string,
    itemSeller: string
    active: boolean
    itemDescription: string
}

export type ChatMessage = {
    messageType: string;
    roomId: string;
    sender: string;
    message: string;
    time: string;
}