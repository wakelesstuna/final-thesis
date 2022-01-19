export const validateUsername = async (e) => {
  return !(await checkIfUsernameIsTaken(e.current.value));
};

export const validatePhone = (e) => {
  if (/[a-zA-Z]/g.test(e.current.value)) {
    return false;
  } else {
    return true;
  }
};

export const validateEmail = async (e) => {
  console.log("email: ", e.current.value);
  if (/^\w+@[a-zA-Z_]+?\.[a-zA-Z]{2,3}$/.test(e.current.value)) {
    return !(await checkIfEmailIsTaken(e.current.value));
  } else {
    return false;
  }
};

export const formatPhoneNumber = (phoneNumber) => {
  if (/[a-zA-Z]/g.test(phoneNumber)) return phoneNumber;
  return phoneNumber.replace(/\D/g, "");
};

export const validatePassword = (e) => {
  if (e.current.value.length < 6) {
    return false;
  }
  return true;
};

const checkIfUsernameIsTaken = async (username) => {
  try {
    let resp = await fetch("http://localhost:8000/graphql", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({
        query: `query {
        exitsByUsername(username: "${username}") 
      }`,
      }),
    });
    resp = await resp.json();
    return resp.data.exitsByUsername;
  } catch (e) {
    console.log({ e });
  }
};

const checkIfEmailIsTaken = async (email) => {
  try {
    let resp = await fetch("http://localhost:8000/graphql", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({
        query: `query {
        exitsByEmail(email: "${email}") 
      }`,
      }),
    });

    resp = await resp.json();
    return resp.data.exitsByEmail;
  } catch (e) {
    console.log({ e });
  }
};

const checkIfPhoneIsTaken = async (phone) => {
  try {
    let resp = await fetch("http://localhost:8000/graphql", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({
        query: `query {
        exitsByPhone(phone: "${phone}") 
      }`,
      }),
    });

    resp = await resp.json();
    return resp.data.exitsByPhone;
  } catch (e) {
    console.log({ e });
  }
};
