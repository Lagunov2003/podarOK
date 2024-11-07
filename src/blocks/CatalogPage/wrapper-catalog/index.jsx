import React from "react";
import "./style.scss";

function WrapperCatalog({ children }) {
    
    return <section className="wrapper-catalog">
        <div className="wrapper-catalog__content">
            {children}
        </div>
    </section>
}

export default WrapperCatalog;