const process = require('process');
const mc = require("minecraft-protocol");

process.stdin.setEncoding('utf8');

process.stdin.on('readable', () => {
    const chunk = process.stdin.read();
    if (chunk != null) {
        process.stdout.write("E:" + chunk + "\n");
    }
    if (chunk !== null && client != null) {
        client.write('chat', {message: chunk.trim()});
    }
});

process.stdin.on('end', () => {
    process.stdout.write('end');
});

const username = process.argv[2] || "monbot";
process.stdout.write('Starting protocol client with username: ' + username + "\n");
const client = mc.createClient({
    username: username,
});

client.on('chat', function (packet) {
    const jsonMsg = JSON.parse(packet.message);
    process.stdout.write("P:" + JSON.stringify(jsonMsg) + "\n");
});