async function send() {
    let object = {
        "code": document.getElementById("code_snippet").value,
        "time": parseInt(document.getElementById("time_restriction").value),
        "views": parseInt(document.getElementById("views_restriction").value)
    };

    let json = JSON.stringify(object);

    const response = await fetch('/code/api/new', {
        method: "POST",
        headers: {
            "Content-Type": "application/json; charset=utf-8",
        },
        body: json
    });

    if (response.ok) {
        const uuid = await response.json();
        const success = document.createElement("p");
        success.innerText = `Success!\n Id = ${uuid.id}`;
        document.body.appendChild(success);
    } else {
        const err = document.createElement("p");
        err.innerText = "Error posting new code !";
        document.body.appendChild(err);
    }
}