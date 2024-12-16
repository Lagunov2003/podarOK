export async function responseRegister(name, email, password) {

    try {
        const response = await fetch("http://localhost:8080/registration", {
            method: "POST",
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({
                "firstName": name,
                "email": email,
                "password": password
            })
        })

        if (response.status == 200) {
            return "успешно"
        } else if (response.status == 409) {
            return "повтор"
        } else {
            return "ошибка"
        }
    } catch (e) {

    }
}

export async function responseLogin(email, password) {

    try {
        const response = await fetch("http://localhost:8080/login", {
            method: "POST",
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({
                "email": email,
                "password": password
            })
        })

        if (response.status == 200) {
            const data = await response.json()
            localStorage.setItem("token", data.token)
            console.log(data.token);
            return "успешно"
        } else {
            return "ошибка"
        }
    } catch (e) {

    }
}

//UserController
export async function responseGetProfile(token) {

    try {
        const response = await fetch("http://localhost:8080/profile", {
            method: "GET",
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${token}`
            },
        })

        if (response.status == 200) {
            const data = await response.json()
            console.log(data);
            return data
        } else {
            return "ошибка"
        }
    } catch (e) {

    }
}

export async function responsePutProfile(token) {

    try {
        const response = await fetch("http://localhost:8080/profile", {
            method: "GET",
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${token}`
            },
        })

        if (response.status == 200) {
            const data = await response.json()
            console.log(data);
            return data
        } else {
            return "ошибка"
        }
    } catch (e) {

    }
}


//Каталог 

export async function responseGetCatalog(setList, setPage, page = 1) {

    let strUrl = "http://localhost:8080/catalog?page=" + page

    try {
        const response = await fetch(strUrl, {
            method: "GET",
            headers: {
                'Content-Type': 'application/json',
            },
        });

        const data = await response.json();
        console.log(data);
        setList(data["_embedded"].giftDtoList)
        setPage(data["page"])
    } catch (e) {

    }
}

export async function responseGetCatalogSearch(setList, setPage, page = 1, search) {

    let strUrl = "http://localhost:8080/catalogSearch?page=" + page + "&query=" + search

    try {
        const response = await fetch(strUrl, {
            method: "GET",
            headers: {
                'Content-Type': 'application/json',
            },
        });

        const data = await response.json();
        console.log(data);
        setList(data["_embedded"].giftDtoList)
        setPage(data["page"])
    } catch (e) {

    }
}

export async function responseGetSortCatalog(setList, setPage, sort) {
    let strUrl = "http://localhost:8080/sortCatalog?sort=" + sort

    try {
        const response = await fetch(strUrl, {
            method: "GET",
            headers: {
                'Content-Type': 'application/json',
            },
        });

        const data = await response.json();
        console.log(data);
        setList(data["_embedded"].giftDtoList)
        setPage(data["page"])
    } catch (e) {

    }
}

export async function responseGetGift(setItem, id) {
    let strUrl = "http://localhost:8080/gift/" + id

    try {
        const response = await fetch(strUrl, {
            method: "GET",
            headers: {
                'Content-Type': 'application/json',
            },
        });

        const data = await response.json();
        console.log(data);
        setItem(data)
    } catch (e) {

    }
}

//Корзина

export async function responseGetCart(token, setList) {
    let strUrl = "http://localhost:8080/cart"

    try {
        const response = await fetch(strUrl, {
            method: "GET",
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${token}`
            },
        });

        const data = await response.json();
        console.log(data);
        setList(data)
    } catch (e) {

    }
}

export async function responsePostCart(token, id, count = 1) {
    let strUrl = "http://localhost:8080/cart"

    try {
        const response = await fetch(strUrl, {
            method: "POST",
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${token}`
            },
            body: JSON.stringify({
                giftId: id,
                itemCount: count
            })
        });

        const data = await response.json();
        console.log(data);
    } catch (e) {

    }
}

export async function responsePutCart(token, id, count) {
    let strUrl = "http://localhost:8080/cart"

    try {
        const response = await fetch(strUrl, {
            method: "PUT",
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${token}`
            },
            body: JSON.stringify({
                giftId: id,
                itemCount: count
            })
        });

        const data = await response.json();
        console.log(data);
    } catch (e) {

    }
}


// Карта от Яндекса 
export const getAdressFromCoordinates = async (lat, lon) => {
    const apiKey = "83f63b8a-a5c6-4cfc-a278-cc18d2859012"
    const response = await fetch(`https://geocode-maps.yandex.ru/1.x/?apikey=${apiKey}&geocode=${lon},${lat}&format=json`);
    const data = await response.json();

    if (
        data.response &&
        data.response.GeoObjectCollection &&
        data.response.GeoObjectCollection.featureMember &&
        data.response.GeoObjectCollection.featureMember.length > 0
    ) {
        const firstGeoObject = data.response.GeoObjectCollection.featureMember[1].GeoObject;
        const address = firstGeoObject.metaDataProperty.GeocoderMetaData.text;
        return address;
    } else {
        return "Address not found";
    }
};