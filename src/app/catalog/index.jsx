import React, { useEffect, useState } from "react";
import Filter from "../../blocks/CatalogPage/filter";
import WrapperCatalog from "../../blocks/CatalogPage/wrapper-catalog";
import BlockCatalog from "../../blocks/CatalogPage/block-catalog";
import List from "../../blocks/CatalogPage/list";
import { useNavigate } from "react-router";
import { responseGetCatalog } from "../../tool/response";

const defaultQuery = {
    search: "",
    page: 0
}


function Catalog() {
    const [query, setQuery] = useState(defaultQuery)
    const [list, setList] = useState([])
    const navigate = useNavigate()

    useEffect(() => {
        const strUrl = `/catalog?search=${query.search}&page=${query.page}`
        navigate(strUrl)

        responseGetCatalog(setList)
    }, [])

    return (
        <WrapperCatalog>
            <BlockCatalog />
            <Filter />
            <List list={list}/>
        </WrapperCatalog>
    );
}

export default Catalog;
