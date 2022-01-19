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

export const uploadIamge = async (userId, file) => {
  let response;
  const formData = new FormData();

  formData.append("file", file, file?.name);

  await axios
    .post(`http://localhost:8005/cdn/server/v1/upload/${userId}`, formData, {
      headers: {
        "Content-Type": "multipart/form-data",
      },
    })
    .then((resp) => {
      response = resp;
    });

  return response;
};
