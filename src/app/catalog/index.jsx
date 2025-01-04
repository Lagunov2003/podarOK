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

const defaultFilter = {
    price: "",
    categories: [],
    occasions: [],
    gender: -1,
    age: -1,
};

function Catalog() {
    const [query, setQuery] = useState(defaultQuery);
    const [list, setList] = useState([]);
    const [page, setPage] = useState(null);
    const [currentPage, setCurrentPage] = useState(1);
    const [search, setSearch] = useState("");
    const [sortValue, setSortValue] = useState("")
    const [filter, setFilter] = useState(defaultFilter)
    const navigate = useNavigate();


    useEffect(() => {
        const nameSort = sessionStorage.getItem("nameSort");

        if (nameSort) {
            setSortValue(nameSort)
            sessionStorage.removeItem("nameSort");
        }
    }, [])


    useEffect(() => {
        ;(async () => {
            let sort = ""
            let filt = {}

            if(sortValue == "По рейтингу") sort = "Выбрать всё"
            else if(sortValue == "Выбрать всё") sort = ""
            else sort = sortValue

            if(filter.age != -1) filt["age"] = filter.age
            if(filter.gender != -1) filt["gender"] = filter.gender
            if(filter.price != "") filt["budget"] = filter.price
            if(filter.categories?.length != 0) filt["categories"] = filter.categories
            if(filter.occasions?.length != 0) filt["occasions"] = filter.occasions
            
            await responseGetCatalog(setList, setPage, currentPage, search, sort, Object.keys(filt)?.length == 0 ? null : filt);
        })();
    }, [currentPage, search, sortValue, filter]);

    return (
        <WrapperCatalog>
            <BlockCatalog search={search} setSearch={setSearch} sortValue={sortValue} setSortValue={setSortValue}/>
            <Filter setFilter={setFilter}/>
            <List list={list} page={page} setCurrentPage={setCurrentPage} currentPage={currentPage} />
        </WrapperCatalog>
    );
}

export default Catalog;
