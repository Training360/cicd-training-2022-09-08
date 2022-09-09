pm.test("Status code is 201", function () {
    pm.response.to.have.status(201);
});
pm.test("Response contains the name", function () {
    var jsonData = pm.response.json();
    pm.expect(jsonData.name).to.eql("John Doe");
});