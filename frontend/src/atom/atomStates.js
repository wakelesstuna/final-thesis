import { atom, selector } from "recoil";

export const atomUser = atom({
  key: "atomUser",
  default: {
    id: "",
    username: "",
    firstName: "",
    lastName: "",
    profilePic: "",
    description: "",
  },
});

export const atomCurrentFile = atom({
  key: "atomCurrentFile",
  default: {},
});

export const atomPostId = atom({
  key: "atomPostId",
  default: "",
});

export const testAtom = selector({
  key: "testAtom",
  get: ({ get }) => {},
});
