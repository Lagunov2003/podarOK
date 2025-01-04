import { jwtDecode } from "jwt-decode";

export function convertPrice(price) {
    return [...price.toString().slice("")]
        .reverse()
        .map((v, i) => ((i + 1) % 3 == 0 ? " " + v : v))
        .reverse()
        .join("")
}

export function convertImg(url) {
    return "/img/photos" + url.slice(1, url.length) + ".png"
}

export function convertDate(date) {
    return date.split("-").reverse().join(".")
}

export function decoderToken(token) {
    const decode = jwtDecode(token)

    if(decode.roles[0] == "ROLE_ADMIN") {
        return true
    } 

    return false
}