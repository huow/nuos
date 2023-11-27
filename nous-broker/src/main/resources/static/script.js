// Get the input element
var searchBox = document.getElementById('searchBox');

// Add an event listener to the input element
searchBox.addEventListener('keyup', function(event) {
    // Check if the key pressed is 'Enter'
    if (event.key === 'Enter') {
        // If it is, perform the desired action
        // For example, calling a function to handle the search
        performSearch();
    }
});

// Define the performSearch function
function performSearch() {
    var searchTerm = searchBox.value;
    alert('Search for "' + searchTerm + '" would be performed here.');
    // Here you would handle the search logic or redirect.
}