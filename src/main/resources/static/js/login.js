import { showToast } from "./toaster.js";
document.addEventListener("DOMContentLoaded", (e) => {
  const form = document.getElementById("loginform");
  form.addEventListener("submit", (e) => {
    e.preventDefault();
    const loader = document.getElementById("loader");
    loader.style.display = "flex";
    const formData = new FormData(form);
    let reqBody = {
      email: formData.get("email"),
      password: formData.get("password"),
    };
    userLogin(reqBody);
    loader.style.display = "none";
  });
});
async function userLogin(reqBody) {
  try {
    const response = await fetch("/api/auth/login", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(reqBody),
    });
    const data = await response.json();
    switch (response.status) {
      case 200:
        showToast(data.message, data.status);
        break;
      case 401:
        showToast(data.message, data.status);
        break;
      case 404:
        showToast(data.message, data.status);
        break;
      case 500:
        showToast(data.message, data.status);
        break;
      default:
        showToast("Something went wrong,Please try again later", "error");
        break;
    }
  } catch (error) {
    showToast(error, "error");
  }
}
