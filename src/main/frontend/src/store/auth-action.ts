import { GET, POST } from "./fetch-auth-action";

const createTokenHeader = (token: string) => {
  return {
    headers: {
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
  minPriceWanted: string
) => {
  const URL = "/items";
  const addItemObject = { title, description, minPriceWanted };
  return POST(URL, addItemObject, createTokenHeader(token));
};
