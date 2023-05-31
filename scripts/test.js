import http from 'k6/http';
export const options = {
    vus: 1,
    duration: '10s',
};
export default function () {
    http.get('https://dekeet.jovisimons.nl');
}