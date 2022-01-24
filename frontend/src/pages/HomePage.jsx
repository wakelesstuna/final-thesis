// React
import { useEffect } from "react";
import { useNavigate } from "react-router-dom";
// Components
import Feed from "../components/feed/Feed";

const HomePage = () => {
  const navigate = useNavigate();
  const user = localStorage.getItem("user");
  useEffect(() => {
    if (user === null) {
      navigate("/login");
    }
  }, []);

  return <div>{user && <Feed />}</div>;
};

export default HomePage;
