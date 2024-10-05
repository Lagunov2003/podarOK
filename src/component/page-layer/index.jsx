import React from "react";
import img from "../../../public/img/circle.jpg";
import "./style.scss"

function PageLayer({ children }) {
  return (
    <div className="page-layer">
      {children}
      <div className="page-layer__bg">
        <img src={img} alt="" />
      </div>
    </div>
  );
}

export default PageLayer;
