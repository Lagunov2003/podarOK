import React from "react";
import Filter from "../../blocks/CatalogPage/filter";
import WrapperCatalog from "../../blocks/CatalogPage/wrapper-catalog";
import BlockCatalog from "../../blocks/CatalogPage/block-catalog";
import List from "../../blocks/CatalogPage/list";

function Catalog() {
    
    return (
        <WrapperCatalog>
            <BlockCatalog />
            <Filter />
            <List />
        </WrapperCatalog>
    );
}

export default Catalog;
