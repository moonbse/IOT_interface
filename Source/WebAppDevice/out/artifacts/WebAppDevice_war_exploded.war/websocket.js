window.onload = init;
var socket = new WebSocket("ws://localhost:8081/WebAppDevice_war_exploded/actions");
socket.onmessage = onMessage;
var updates;
function onClose() {
    clearInterval(updates);
}

socket.onclose = onClose;


var flag = 1;
const url = "http://localhost:8080/api/devices";
function onMessage(event) {
    var device = JSON.parse(event.data);
    if (device.action === "add") {
        printDeviceElement(device);
        if(flag === 1){
            updates = setInterval(api_access, 600);
            flag = 0;
        }
    }
    if (device.action === "remove") {
        document.getElementById(device.id).remove();
        //device.parentNode.removeChild(device);
    }
    if (device.action === "toggle") {
        var node = document.getElementById(device.id);
        var statusText = node.children[2];
        if (device.status === "On") {
            statusText.innerHTML = "Status: " + device.status + " (<a href=\"#\" OnClick=toggleDevice(" + device.id + ")>Turn off</a>)";
        } else if (device.status === "Off") {
            statusText.innerHTML = "Status: " + device.status + " (<a href=\"#\" OnClick=toggleDevice(" + device.id + ")>Turn on</a>)";
        }
    }
}

function api_access(){
    //get data
    fetch(url)
        .then((resp)=>resp.json())
        .then(function(data){
            for(var i = 0; i< data.length; i++){
                var obj = data[i];
                var id = obj['id'];
                var node = document.getElementById(id);
                var d1 = node.children[4];
                var d2 = node.children[5];
                var d3 = node.children[6];
                d1.innerHTML = "<b>data1:</b> " + obj['data1'];
                d2.innerHTML = "<b>data2:</b> " + obj['data2'];
                d3.innerHTML = "<b>data3:</b> " + obj['data3'];
            }
        }).catch(function(error){
            console.log(error);
    });
}

//periodically access the api and get json data, and change the values
// function update_data(data){
//    var device = JSON.parse(data);
//    var node = document.getElementById(device.id);
//    // var d1 = node.children[]
// }
function addDevice(name, type, description) {
    var DeviceAction = {
        action: "add",
        name: name,
        type: type,
        description: description
    };
    socket.send(JSON.stringify(DeviceAction));
}

function removeDevice(element) {
    var id = element;
    var DeviceAction = {
        action: "remove",
        id: id
    };
    socket.send(JSON.stringify(DeviceAction));
}

function toggleDevice(element) {
    var id = element;
    var DeviceAction = {
        action: "toggle",
        id: id
    };
    socket.send(JSON.stringify(DeviceAction));
}

function printDeviceElement(device) {
    var content = document.getElementById("content");

    var deviceDiv = document.createElement("div");
    deviceDiv.setAttribute("id", device.id);
    deviceDiv.setAttribute("class", "device " + device.type);
    content.appendChild(deviceDiv);

    var deviceName = document.createElement("span");
    deviceName.setAttribute("class", "deviceName");
    deviceName.innerHTML = device.name;
    deviceDiv.appendChild(deviceName);

    var deviceType = document.createElement("span");
    deviceType.innerHTML = "<b>Type:</b> " + device.type;
    deviceDiv.appendChild(deviceType);

    var deviceStatus = document.createElement("span");
    if (device.status === "On") {
        deviceStatus.innerHTML = "<b>Status:</b> " + device.status + " (<a href=\"#\" OnClick=toggleDevice(" + device.id + ")>Turn off</a>)";
    } else if (device.status === "Off") {
        deviceStatus.innerHTML = "<b>Status:</b> " + device.status + " (<a href=\"#\" OnClick=toggleDevice(" + device.id + ")>Turn on</a>)";
        //deviceDiv.setAttribute("class", "device off");
    }
    deviceDiv.appendChild(deviceStatus);

    var deviceDescription = document.createElement("span");
    deviceDescription.innerHTML = "<b>Comments:</b> " + device.description;
    deviceDiv.appendChild(deviceDescription);

    var data1 = document.createElement("span");
    data1.innerHTML = "<b>data1:</b> " + 0;
    deviceDiv.appendChild(data1);

    var data2 = document.createElement("span");
    data2.innerHTML = "<b>data1:</b> " + 0;
    deviceDiv.appendChild(data2);

    var data3 = document.createElement("span");
    data3.innerHTML = "<b>data1:</b> " + 0;
    deviceDiv.appendChild(data3);

    var removeDevice = document.createElement("span");
    removeDevice.setAttribute("class", "removeDevice");
    removeDevice.innerHTML = "<a href=\"#\" OnClick=removeDevice(" + device.id + ")>Remove device</a>";
    deviceDiv.appendChild(removeDevice);
}

function showForm() {
    document.getElementById("addDeviceForm").style.display = '';
}

function hideForm() {
    document.getElementById("addDeviceForm").style.display = "none";
}

function formSubmit() {
    var form = document.getElementById("addDeviceForm");
    var name = form.elements["device_name"].value;
    var type = form.elements["device_type"].value;
    var description = form.elements["device_description"].value;
    hideForm();
    document.getElementById("addDeviceForm").reset();
    addDevice(name, type, description);
}

function init() {
    hideForm();
}