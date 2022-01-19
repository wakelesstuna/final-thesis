// React
import { useEffect } from "react";
// Components
import Swal from "sweetalert2";
import withReactContent from "sweetalert2-react-content";

const ErrorSwalMessage = ({ error }) => {
  const MySwal = withReactContent(Swal);
  useEffect(() => {
    MySwal.fire({
      title: <strong>Error</strong>,
      html: <i>{error.message}</i>,
      icon: "error",
    });
  }, [null]);
  return <div></div>;
};

export default ErrorSwalMessage;
