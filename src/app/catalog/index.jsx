import React from "react";
import Filter from "../../blocks/CatalogPage/filter";
import WrapperCatalog from "../../blocks/CatalogPage/wrapper-catalog";
import BlockCatalog from "../../blocks/CatalogPage/block-catalog";

function Catalog() {
    
    return (
        <WrapperCatalog>
            <BlockCatalog />
            <Filter />
        </WrapperCatalog>
    );
}

export default Catalog;
