export async function getResponse() {

}

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