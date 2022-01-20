const axios = require("axios");

/**
 * This file holds util functions for the application
 */

export const formatNumber = (number) => {
  return number.toLocaleString("sv-SE", {
    minimumFractionDigits: 0,
    maximumFractionDigits: 0,
  });
};

export const uploadImage = async (userId, file) => {
  let response;
  const formData = new FormData();

  formData.append("file", file, file?.name);

  await axios
    .post(`${process.env.REACT_APP_IMAGE_UPLOAD_URL}${userId}`, formData, {
      headers: {
        "Content-Type": "multipart/form-data",
      },
    })
    .then((resp) => {
      response = resp;
    });

  return response;
};

export const uploadVideo = async (userId, file) => {
  let response;
  const formData = new FormData();

  formData.append("file", file, file?.name);

  await axios
    .post(`${process.env.REACT_APP_STORY_UPLOAD_URL}${userId}`, formData, {
      headers: {
        "Content-Type": "multipart/form-data",
      },
    })
    .then((resp) => {
      response = resp;
    });

  return response;
};
