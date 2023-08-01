import { GET, POST, PUT } from "./fetch-auth-action";

const createTokenHeader = (token: string) => {
  return {
    headers: {
      Authorization: "Bearer " + token,
    },
  };
};

const createMultipartTokenHeader = (token: string) => {
  return {
    headers: {
      'Content-Type': 'multipart/form-data',
      Authorization: "Bearer " + token,
    },
  };
};

const calculateRemainingTime = (expirationTime: number) => {
  const currentTime = new Date().getTime();
  const adjExpirationTime = new Date(expirationTime).getTime();
  return adjExpirationTime - currentTime;
};

export const loginTokenHandler = (
  accessToken: string,
  expirationTime: number
) => {
  localStorage.setItem("token", accessToken);
  localStorage.setItem("expirationTime", String(expirationTime));

  return calculateRemainingTime(expirationTime);
};

export const retrieveStoredToken = () => {
  const storedToken = localStorage.getItem("token");
  const storedExpirationDate = localStorage.getItem("expirationTime") || "0";

  const remaingTime = calculateRemainingTime(+storedExpirationDate);

  if (remaingTime <= 1000) {
    localStorage.removeItem("token");
    localStorage.removeItem("expirationTime");
    return null;
  }

  return {
    token: storedToken,
    duration: remaingTime,
  };
};

export const signupActionHandler = (
  username: string,
  password: string,
  passwordCheck: string,
  nickname: string
) => {
  const URL = "user/auth/signup";
  const signupObjcect = { username, password, passwordCheck, nickname };
  return POST(URL, signupObjcect, {});
};

export const loginActionHandler = (username: string, password: string) => {
  const URL = "user/auth/login";
  const loginObject = { username, password };
  return POST(URL, loginObject, {});
};

export const getUserActionHandler = (token: string) => {
  const URL = "/user/my/profile";
  return GET(URL, createTokenHeader(token));
};

export const logoutActionHandler = () => {
  localStorage.removeItem("token");
  localStorage.removeItem("expirationTime");
};

export const addItemHandler = (
  token: string,
  title: string,
  description: string,
  minPriceWanted: string,
  file: File | null
) => {
  const URL = "/items";
  const addItemObject = { title, description, minPriceWanted };

  const formData = new FormData();
  formData.append('requestSalesItemDto', new Blob([JSON.stringify(addItemObject)], { type: "application/json" }));


  if (file != null) {
    formData.append('file', file);
  }
  return POST(URL, formData, createMultipartTokenHeader(token));
};

export const updateProposal = (
  token: string,
  itemId: number,
  negotiationId: number,
  status: string
) => {
  const url = '/items/' + itemId + '/proposal/' + negotiationId;
  const updateProposalObject = { status }
  return PUT(url, updateProposalObject, createTokenHeader(token));
}

export const addComment = (
  token: string,
  itemId: number,
  content: string
) => {
  const url = '/items/' + itemId + '/comments';
  const commentObject = { content }
  return POST(url, commentObject, createTokenHeader(token));
}

export const replyComment = (
  token: string,
  itemId: number,
  commentId: number,
  reply: string
) => {
  const url = '/items/' + itemId + '/comments/' + commentId + '/reply';
  const replyCommentObject = { reply }
  return PUT(url, replyCommentObject, createTokenHeader(token));
}


export const addNegotiation = (
  token: string,
  itemId: number,
  suggestedPrice: string
) => {
  const url = '/items/' + itemId + '/proposal';
  console.log("??"+url);
  const addNegotiationObject = { suggestedPrice }
  return POST(url, addNegotiationObject, createTokenHeader(token));
}

export const getEachItemHandler = (
  token: string,
  itemId: string
) => {
  const url = '/items/' + itemId;
  return GET(url, createTokenHeader(token));
}

export const getItemProposalHandler = (
  token: string,
  itemId: string
) => {
  const url = '/items/' + itemId + '/proposal';
  return GET(url, createTokenHeader(token));
}