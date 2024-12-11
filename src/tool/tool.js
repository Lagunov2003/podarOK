
export function convertPrice(price) {
    return [...price.toString().slice("")]
        .reverse()
        .map((v, i) => ((i + 1) % 3 == 0 ? " " + v : v))
        .reverse()
        .join("")
}