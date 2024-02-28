//测试环境
// const url = 'http://192.168.10.239:8080';
//正式环境
// const url = 'http://cloud.gsitsystem.com';
const url = getContextPath();

function getPortUrl() {
    return url;
}

function getContextPath() {
    let pathUrl = document.location.pathname;
    let index = pathUrl.substr(1).indexOf("/so");
    let result = pathUrl.substr(0, index + 1);
    return result;
}