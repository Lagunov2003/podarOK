import React from "react";
import "./style.scss";

function LoadingCircle({ loading, children }) {
    return (
        <>
            {loading === false ? (
                <div className="loading-circle">
                    <svg className="spinner" width="100px" height="100px" viewBox="0 0 100 100" xmlns="http://www.w3.org/2000/svg">
                        <circle className="path" fill="none" cx="50" cy="50" r="45"></circle>
                    </svg>
                </div>
            ) : (
                <>{children}</>
            )}
        </>
    );
}

export default LoadingCircle;
