import { showToast } from "./toaster.js";
document.addEventListener("DOMContentLoaded", (e) => {
  const form = document.getElementById("signup");
  form.addEventListener("submit", (e) => {
    e.preventDefault();
    const loader = document.getElementById("loader");
    loader.style.display = "flex";
    let formData = new FormData(form);
    let reqBody = {
      username: formData.get("username"),
      email: formData.get("email"),
      password: formData.get("password"),
      role: "USER",
      emailVerified: true,
    };
    registerUser(reqBody);
    loader.style.display = "none";
  });
});

async function registerUser(reqBody) {
  try {
    const response = await fetch("/api/auth/register", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(reqBody),
    });
    const data = await response.json();
    switch (response.status) {
      case 201:
        showToast(data.message, data.status);
        window.location.href = "/login";
        break;
      case 409:
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
