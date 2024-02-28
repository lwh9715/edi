//派送方式
function getDeliverystatus(e) {
    if (e.value == 'A') {
        return '在途中';
    } else if (e.value == 'B') {
        return '已收货';
    } else if (e.value == 'B') {
        return '已收货';
    } else if (e.value == 'C') {
        return '疑难';
    } else if (e.value == 'D') {
        return '已送达';
    } else if (e.value == 'E') {
        return '退签';
    } else if (e.value == 'F') {
        return '派送中';
    } else if (e.value == 'G') {
        return '退回';
    } else if (e.value == 'H') {
        return '转单';
    } else if (e.value == 'I') {
        return '部分送达';
    } else {
        return '';
    }
}

//派送状态
function getDeliveryway(e) {
    if (e.value == 'Express') {
        return '快递派送';
    } else if (e.value == 'Truck') {
        return '卡车派送';
    } else if (e.value == 'Carpool') {
        return '拼车派送';
    } else if (e.value == 'Complete') {
        return '整车派送';
    } else if (e.value == 'Container') {
        return '整柜派送';
    } else if (e.value == 'Customer') {
        return '客户自提';
    } else {
        return '';
    }
}

//业务类型转换
function getLdtype2(e) {
    if (e.value == 'H') {
        return 'FBA海运';
    } else if (e.value == 'K') {
        return 'FBA空运';
    } else if (e.value == 'T') {
        return 'FBA铁运';
    } else if (e.value == 'X') {
        return '快递/专线';
    } else if (e.value == 'F') {
        return '整柜';
    } else if (e.value == 'A') {
        return '空运委托';
    }
}

//应收标识转换
function arStatus(e) {
    if (e.value === undefined) {
        return '';
    } else if (e.value) {
        return '有应收';
    } else {
        return '无应收';
    }
}

//应付标识转换
function apStatus(e) {
    if (e.value === undefined) {
        return '';
    } else if (e.value) {
        return '有应付';
    } else {
        return '无应付';
    }
}

//应付标识转换
function arapStatus(e) {
    if (e.value === undefined) {
        return '';
    } else if (e.value) {
        return '是';
    } else {
        return '否';
    }
}

//标识转换
function getHbl(e) {
    if (e.value === undefined) {
        return '';
    } else if (e.value == null) {
        return 'M';
    } else if (e.value > 0) {
        return 'H';
    } else {
        return '';
    }
}

function numberToColor(val) {
    if (val === 0) {
        return '';
    } else if (val === undefined) {
        return '';
    } else if (val === null) {
        return '#d9534f';
    } else {
        return '#5cb85c';
    }
}

function cellStyle(params) {
    const color = this.numberToColor(params.value);
    return {
        backgroundColor: color,
        textAlign: 'center'
    };
}

function subtractStyle() {
    return {
        color: '#FFF',
        borderColor: '#d1504d',
        backgroundColor: '#d1504d',
        textAlign: 'center'
    };
}

function plusStyle() {
    return {
        color: '#FFF',
        borderColor: '#4caf50',
        backgroundColor: '#4caf50',
        textAlign: 'center'
    };
}


function trackStyle(params) {
    return {
        textAlign: 'center',
        color: '#fff',
        background: '#4caf50',
        borderColor: '#4caf50'
    };
}

//子单icon
function mergeHblIcon(e) {
    if (e.value != '') {
        return 'X';
    }
}

//子单icon
function mergeHblPlusIcon(e) {
    if (e.value != '') {
        return '十';
    }
}

function numberParser(params) {
    const newValue = params.newValue;
    let valueAsNumber;
    if (newValue === null || newValue === undefined || newValue === '') {
        valueAsNumber = null;
    } else {
        valueAsNumber = parseFloat(params.newValue);
    }
    return valueAsNumber;
}

//NOS标识转换
function getNos(e) {
    if (e.value.includes('TAEE') || e.value.includes('TSZE') || e.value.includes('TDAE')) {
        return '(H) ' + e.value;
    } else if (e.value.includes('TAES') || e.value.includes('TSZS') || e.value.includes('TDAS')) {
        return '(M) ' + e.value;
    } else {
        return e.value;
    }
}

//工作单类型
function getJobType(e) {
    if (e.jobtype === 'S') {
        return '海运';
    } else if (e.jobtype === 'A') {
        return '空运';
    } else if (e.jobtype === 'L') {
        return '陆运';
    } else if (e.jobtype === 'D') {
        if (e.ldtype2 === 'H') {
            return 'FBA海运';
        } else if (e.ldtype2 === 'K') {
            return 'FBA空运';
        } else if (e.ldtype2 === 'T') {
            return 'FBA铁运';
        } else if (e.ldtype2 === 'X') {
            return '快递/专线';
        }
    }
}

//时间转换
function formatDate(row, column, cellValue, index) {
    if (cellValue == null || cellValue == "") return "";
    let date = new Date(cellValue);//时间戳为10位需*1000，如果为13位的话不需乘1000。
    let Y = date.getFullYear() + '-';
    let M = date.getMonth() + 1 < 10 ? '0' + (date.getMonth() + 1) + '-' : date.getMonth() + 1 + '-';
    let D = date.getDate() < 10 ? '0' + date.getDate() + ' ' : date.getDate() + ' ';
    let h = date.getHours() < 10 ? '0' + date.getHours() + ':' : date.getHours() + ':';
    let m = date.getMinutes() < 10 ? '0' + date.getMinutes() : date.getMinutes();
    return Y + M + D + h + m;
}

function getCheckName(e) {
    if (e === 0) {
        return '未审核';
    } else if (e === 1) {
        return '审核通过';
    } else if (e === 2) {
        return '审核不通过';
    }
}

function getCheckStatus(e) {
    if (e === 0) {
        return '';
    } else if (e === 1) {
        return 'success';
    } else if (e === 2) {
        return 'danger';
    }
}

//业务类型转换
function getConfirm(e) {

    if (e.value === undefined) {
        return '';
    } else if (e.value) {
        return '已确认';
    } else {
        return '未确认';
    }
}

function conversionTime(params) {
    if (params.value == null || params.value == "") return "";
    let date = new Date(params.value);
    let Y = date.getFullYear() + '-';
    let M = date.getMonth() + 1 < 10 ? '0' + (date.getMonth() + 1) + '-' : date.getMonth() + 1 + '-';
    let D = date.getDate() < 10 ? '0' + date.getDate() + ' ' : date.getDate() + ' ';
    let h = date.getHours() < 10 ? '0' + date.getHours() + ':' : date.getHours() + ':';
    let m = date.getMinutes() < 10 ? '0' + date.getMinutes() : date.getMinutes();
    return Y + M + D + h + m;
}

function checkTrace(e) {
    if (e.value !== 0) {
        return 'TRACK';
    }
}


function formateArApStatus(e) {
    if (e.data.araptype == '应收') {
        if (e.data.amtstl3 == e.data.amount && e.data.amount != 0) {
            return '已收款';
        } else if (e.data.amount > e.data.amtstl3 && e.data.amtstl3 != 0) {
            return '部分收款';
        } else {
            return '未收款';
        }
    } else if (e.data.araptype == '应付') {
        if (e.data.amtstl3 != 0) {
            return '已付款';
        } else {
            return '未付款';
        }
    }
}

function arApStatusToColor(val) {
    if (val.value === '应收') {
        return {
            backgroundColor: '#4caf50'
        };
    } else if (val.value === '应付') {
        return {
            backgroundColor: '#d9534f'
        };
    }
}
