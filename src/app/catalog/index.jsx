import React, { useEffect, useState } from "react";
import Filter from "../../blocks/CatalogPage/filter";
import WrapperCatalog from "../../blocks/CatalogPage/wrapper-catalog";
import BlockCatalog from "../../blocks/CatalogPage/block-catalog";
import List from "../../blocks/CatalogPage/list";
import { useNavigate } from "react-router";
import { responseGetCatalog, responseGetCatalogSearch, responseGetSortCatalog } from "../../tool/response";

const defaultQuery = {
    search: "",
    page: 0,
};

function Catalog() {
    const [query, setQuery] = useState(defaultQuery);
    const [list, setList] = useState([]);
    const [page, setPage] = useState(null);
    const [currentPage, setCurrentPage] = useState(1);
    const [search, setSearch] = useState("");
    const [sortValue, setSortValue] = useState("")
    const navigate = useNavigate();


    useEffect(() => {
        const nameSort = sessionStorage.getItem("nameSort");

        if (nameSort) {
            setSortValue(nameSort)
            sessionStorage.removeItem("nameSort");
        }
    }, [])

    useEffect(() => {
        // const strUrl = `/catalog?search=${query.search}&page=${query.page}`
        // navigate(strUrl)
        if(search != "") {
            responseGetCatalogSearch(setList, setPage, currentPage, search)
        } else if(sortValue != "" && sortValue != "Выбрать всё") {
            responseGetSortCatalog(setList, setPage, sortValue)
        } else {
            responseGetCatalog(setList, setPage, currentPage);
        }
    }, [currentPage, search, sortValue]);

    return (
        <WrapperCatalog>
            <BlockCatalog search={search} setSearch={setSearch} sortValue={sortValue} setSortValue={setSortValue}/>
            <Filter />
            <List list={list} page={page} setCurrentPage={setCurrentPage} currentPage={currentPage} />
        </WrapperCatalog>
    );
}

export default Catalog;
