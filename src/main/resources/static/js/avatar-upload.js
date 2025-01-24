function validateFileSize(event) {
    const fileInput = event.target;
    const previewImage = document.getElementById('previewImage');

    // Max file size: 2 MB
    const maxSize = 2 * 1024 * 1024;

    if (fileInput.files && fileInput.files[0]) {
        const file = fileInput.files[0];

        if (file.size > maxSize) {
            // Reset selected file
            previewImage.src = '#';
            previewImage.style.display = 'none';
            fileInput.value = ''; // Remove selected file
            return;
        }

        // Show preview
        const reader = new FileReader();
        reader.onload = function (e) {
            previewImage.src = e.target.result;
            previewImage.style.display = 'block';
        };
        reader.readAsDataURL(file);
    } else {
        // If no file is selected, hide preview
        previewImage.src = '#';
        previewImage.style.display = 'none';
    }
}