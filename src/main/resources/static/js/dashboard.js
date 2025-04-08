document.addEventListener("DOMContentLoaded", (e) => {
  const workFlowBtn = document.getElementById("createWorkflowBtn");
  workFlowBtn.addEventListener("click", (e) => {
    fetchAuthUrl();
  });
});
async function fetchAuthUrl() {
  const response = await fetch("/api/google/auth");
  const data = await response.json();
  if (data.redirect) {
    const popup = window.open(
      data.redirect,
      "google-oauth",
      "width=500,height=600"
    );
  }
}
