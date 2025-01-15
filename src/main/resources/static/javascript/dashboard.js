document.addEventListener("DOMContentLoaded", () => {
  const restaurantId = localStorage.getItem("restaurantIdAdmin");
  fetchDashboardData(restaurantId);
  const logoutButton = document.getElementById("logout");
  logoutButton.addEventListener("click", ()=>{
	localStorage.removeItem("emailAdmin");
	localStorage.removeItem("restaurantIdAdmin");
	localStorage.removeItem("usernameAdmin");
	localStorage.removeItem("isLoggedInAdmin");
	window.location.href = "/index";
  });
  	
  const displayArea = document.getElementById("displayArea");
	
  document.querySelectorAll("[data-form]").forEach((option) => {
    option.addEventListener("click", (event) => {
      event.preventDefault();
	  const canvas = document.getElementById('pieChart');
	      if (canvas) {
	          canvas.remove(); // Remove the canvas if it exists
	      }
      const formType = option.getAttribute("data-form");
      displayForm(formType);
    });
  });

  document.querySelectorAll("[data-table]").forEach((option) => {
    option.addEventListener("click", (event) => {
      event.preventDefault();
	  const canvas = document.getElementById('pieChart');
	      if (canvas) {
	          canvas.remove(); // Remove the canvas if it exists
	      }
      const tableType = option.getAttribute("data-table");
      displayTable(tableType);
    });
  });
  
  

  function displayForm(formType) {
    let formHTML = "";
    switch (formType) {
      case "admin":
        formHTML = `
          <div class="form-container">
            <h2>Admin Details</h2>
            <form id="adminForm">
              <label for="email">Email</label>
              <input type="email" id="email" placeholder="Enter your email" required>
              <label for="username">Username</label>
              <input type="text" id="username" placeholder="Enter your username" required>
              <label for="password">Password</label>
              <input type="password" id="password" placeholder="Enter password" required>
              <div>
                <button type="submit" class="submit">Add</button>
                <button type="reset" class="cancel">Reset</button>
              </div>
            </form>
          </div>
        `;
        break;
		case "addMenuItem":
		    formHTML = `
		      <div class="form-container">
		        <h2>Add Menu Item</h2>
		        <form id="menuItemForm" enctype="multipart/form-data">
		          <label for="name">Name</label>
		          <input type="text" id="name" placeholder="Enter item name" required>
		          
		          <label for="price">Price</label>
		          <input type="number" id="price" placeholder="Enter item price" required>
		          
		          <label for="stock">Stock</label>
		          <input type="number" id="stock" placeholder="Enter stock quantity" required>
		          
		          <label for="description">Description</label>
		          <textarea id="description" placeholder="Enter item description" required></textarea>
		          
		          <label for="images">Images</label>
		          <input type="file" id="images" accept="image/*" multiple required>
		          
		          <input type="hidden" id="restaurantId" value="${restaurantId}">
		          
		          <div>
		            <button type="submit" class="submit">Add item</button>
		            <button type="reset" class="cancel">Reset</button>
		          </div>
		        </form>
		      </div>
		    `;
		    break;

      case "category":
        formHTML = `
          <div class="form-container">
            <h2>Category Details</h2>
            <form id="categoryForm">
              <label for="categoryName">Category Name</label>
              <input type="text" id="categoryName" placeholder="Enter category name" required>
              <label for="categoryDescription">Description</label>
              <textarea id="categoryDescription" placeholder="Enter description"></textarea>
              <div>
                <button type="submit" class="submit">Submit</button>
                <button type="button" class="cancel">Cancel</button>
              </div>
            </form>
          </div>
        `;
        break;
      default:
        formHTML = "<p>Form not found.</p>";
    }
    displayArea.innerHTML = formHTML;

    if (formType === "admin") {
      const adminForm = document.getElementById("adminForm");
      adminForm.addEventListener("submit", handleAdminFormSubmit);
    }
	else if(formType == "addMenuItem"){
		const menuItemForm = document.getElementById("menuItemForm");
		menuItemForm.addEventListener("submit", handleMenuItemFormSubmit);
	}
  }

  function displayTable(tableType) {
    let tableHTML = "";
    switch (tableType) {
      case "admin":
        tableHTML = `
          <div class="table-container">
           
            <table class="admin-table"> 
              <thead>
                <tr>
                  <th>ID</th>
                  <th>Username</th>
                  <th>Email</th>
                  <th>Actions</th>
                </tr>
              </thead>
              <tbody>
              </tbody>
            </table>
          </div>
        `;
        break;
		case "reviews":
			
		      tableHTML = `
		        <div class="table-container">
		          
		          <table class="reviews-table">
		            <thead>
		              <tr>
		                <th>ID</th>
		                <th>Review</th>
		                <th>Date</th>
		                <th>Rating</th>
		                <th>Response</th>
		                <th>Actions</th>
		              </tr>
		            </thead>
		            <tbody>
		            </tbody>
		          </table>
		        </div>
		      `;
		      break;
			  case "userStats":
			    tableHTML = `
			      <div class="table-container">
			        
			        <table class="user-stats-table">
			          <thead>
			            <tr>
			              <th>User ID</th>
			              <th>Total Orders</th>
			              <th>Orders in Last 7 Days</th>
			              <th>Orders in Last 30 Days</th>
			              <th>Orders in Last 365 Days</th>
			              <th>Avg Orders Per Week</th>
			              <th>Avg Orders Per Month</th>
			              <th>Avg Orders Per Year</th>
			            </tr>
			          </thead>
			          <tbody>
			          </tbody>
			        </table>
			      </div>
			    `;
			    break;
				case "dailyReport":
				            tableHTML = `
				                <div class="table-container">
				                  
				                    <table class="daily-report-table">
				                        <thead>
				                            <tr>
				                                <th>Report Date</th>
				                                <th>Total Orders</th>
				                                <th>Total Order Value</th>
				                                <th>Completed Orders</th>
				                                <th>Pending Orders</th>
				                                <th>Canceled Orders</th>
				                            </tr>
				                        </thead>
				                        <tbody>
				                        </tbody>
				                    </table>
				                </div>
				            `;
				            break;
			case "weeklyReport":
							            tableHTML = `
							                <div class="table-container">
							                    
							                    <table class="daily-report-table">
							                        <thead>
							                            <tr>
							                                <th>Year</th>
							                                <th>Week</th>
															<th>Total Order</th>
							                                <th>Total Order Value</th>
							                                <th>Completed Orders</th>
							                                <th>Pending Orders</th>
							                                <th>Canceled Orders</th>
							                            </tr>
							                        </thead>
							                        <tbody>
							                        </tbody>
							                    </table>
							                </div>
							            `;
							            break;
			case "monthlyReport":
							            tableHTML = `
							                <div class="table-container">
							                   
							                    <table class="daily-report-table">
							                        <thead>
							                            <tr>
							                                <th>Year</th>
							                                <th>Month</th>
															<th>Total Orders</th>
							                                <th>Total Order Value</th>
							                                <th>Completed Orders</th>
							                                <th>Pending Orders</th>
							                                <th>Canceled Orders</th>
							                            </tr>
							                        </thead>
							                        <tbody>
							                        </tbody>
							                    </table>
							                </div>
							            `;
							            break;
				case "menuItemsTable":
				    tableHTML = `
				        <div class="table-container">
				           
				            <table class="menu-items-table">
				                <thead>
				                    <tr>
				                        <th>Item ID</th>
				                        <th>Name</th>
				                        <th>Description</th>
				                        <th>Price</th>
				                        <th>Stock</th>
				                        <th>Actions</th>
				                    </tr>
				                </thead>
				                <tbody>
				                    <!-- Rows will be dynamically added here -->
				                </tbody>
				            </table>
				        </div>
				    `;
				    break;
					case "orders":
					    tableHTML = `
					        <div class="table-container">
					           
					            <div class="filter-container">
					                <label for="orderStatusFilter">Filter by Status:</label>
					                <select id="orderStatusFilter">
					                    <option value="waiting">Waiting</option>
					                    <option value="preparing">Preparing</option>
					                  
					                    <option value="dispatched">Dispatched</option>
					                </select>
					            </div>
					            <table class="order-items-table">
					                <thead>
					                    <tr>
					                        <th>Item ID</th>
					                        <th>Name</th>
					                        <th>Quantity</th>
					                        <th>Special Request</th>
					                        <th>Price</th>
					                        <th>Actions</th>
					                    </tr>
					                </thead>
					                <tbody>
					                    <!-- Rows will be dynamically added here -->
					                </tbody>
					            </table>
					        </div>
					    `;
				    break;
				case "preferences":
					tableHTML = `
					       <div class="table-container">
					           
					           <table class="customer-preferences-table">
					               <thead>
					                   <tr>
					                       <th>Dish Name</th>
					                       <th>Order Count</th>
					                       <th>Customizations</th>
					                       <th>Percentage of Total Orders</th>
					                   </tr>
					               </thead>
					               <tbody>
					                   <!-- Rows will be dynamically added here -->
					               </tbody>
					           </table>
					       </div>
					   `;
					   break;


    }
    
    displayArea.innerHTML = tableHTML;

    const restaurantId = localStorage.getItem("restaurantIdAdmin");

    if (!restaurantId) {
      alert("Restaurant ID not found. Please register the restaurant first.");
      return;
    }

    // Admin Table Logic
    if (tableType === "admin") {
      fetch(`/get-admins/${restaurantId}`)
        .then(response => {
          if (!response.ok) {
            throw new Error(`HTTP error! Status: ${response.status}`);
          }
          return response.json();
        })
        .then(data => {
          const tbody = document.querySelector('.admin-table tbody');
          tbody.innerHTML = '';
          data.forEach((admin, index) => {
            const row = document.createElement('tr');
            row.innerHTML = `
              <td>${index + 1}</td>
              <td>${admin.username}</td>
              <td>${admin.email}</td>
              <td>
                <button class="delete-admin">Delete</button>
                <input type="hidden" class="admin-id" value="${admin.adminId}">
              </td>
            `;
            tbody.appendChild(row);
          });

          // Handle delete button click
          const deleteButtons = document.querySelectorAll('.delete-admin');
          deleteButtons.forEach(button => {
            button.addEventListener('click', async (event) => {
              const row = event.target.closest('tr');
              const adminId = row.querySelector('.admin-id').value;

              const confirmDelete = confirm("Are you sure you want to delete this admin?");
              if (confirmDelete) {
                try {
                  const response = await fetch(`/delete-admin/${adminId}`, {
                    method: 'DELETE',
                  });

                  if (!response.ok) {
                    throw new Error(`HTTP error! Status: ${response.status}`);
                  }

                  row.remove(); // Remove the row from the table
                  alert("Admin deleted successfully!");
                } catch (error) {
                  console.error('Error deleting admin:', error);
                  alert('Failed to delete admin.');
                }
              }
            });
          });
        })
        .catch(error => {
          console.error('Error fetching admin data:', error);
          alert('Failed to load admin table.');
        });
    }

    // Reviews Table Logic
	

	  if (tableType === "reviews") {
		let restaurantId = localStorage.getItem("restaurantIdAdmin");
	    fetch(`/get-reviews/${restaurantId}`, {
	      method: "GET",
	    })
	      .then(response => {
	        if (!response.ok) {
	          throw new Error(`HTTP error! Status: ${response.status}`);
	        }
	        return response.json();
	      })
	      .then(data => {
	        const tbody = document.querySelector('.reviews-table tbody');
	        tbody.innerHTML = '';
	        data.forEach((review, index) => {
	          const row = document.createElement('tr');
	          row.innerHTML = `
	            <td>${index + 1}</td>
	            <td>${review.reviewText}</td>
	            <td>${review.date}</td>
	            <td>${review.rating}</td>
	            <td>
	              <textarea class="review-response">${review.reviewResponse || ""}</textarea>
	            </td>
	            <td>
	              <button class="update-review">Update</button>
	              <!-- Hidden fields to store full review data -->
	              <input type="hidden" class="review-id" value="${review.reviewId}">
	              <input type="hidden" class="user-id" value="${review.userId}">
	              <input type="hidden" class="restaurant-id" value="${review.restaurantId}">
	              <input type="hidden" class="order-id" value="${review.orderId}">
	              <input type="hidden" class="rating" value="${review.rating}">
	              <input type="hidden" class="review-text" value="${review.reviewText}">
	              <input type="hidden" class="review-date" value="${review.date}">
	            </td>
	          `;
	          tbody.appendChild(row);
	        });

	        // Handle update button click
	        const updateButtons = document.querySelectorAll('.update-review');
	        updateButtons.forEach(button => {
	          button.addEventListener('click', async (event) => {
	            const row = event.target.closest('tr');
	            const reviewPayload = {
	              reviewId: row.querySelector('.review-id').value,
	              userId: row.querySelector('.user-id').value,
	              restaurantId: row.querySelector('.restaurant-id').value,
	              orderId: row.querySelector('.order-id').value,
	              rating: row.querySelector('.rating').value,
	              reviewText: row.querySelector('.review-text').value,
	              date: row.querySelector('.review-date').value,
	              reviewResponse: row.querySelector('.review-response').value,
	            };

	            try {
	              const response = await fetch(`/update-review`, {
	                method: 'POST',
	                headers: {
	                  'Content-Type': 'application/json',
	                },
	                body: JSON.stringify(reviewPayload),
	              });

	              if (!response.ok) {
	                throw new Error(`HTTP error! Status: ${response.status}`);
	              }

	              alert("Review updated successfully!");
	            } catch (error) {
	              console.error('Error updating review:', error);
	              alert('Failed to update review.');
	            }
	          });
	        });
	      })
	      .catch(error => {
	        console.error('Error fetching reviews data:', error);
	        alert('Failed to load reviews table.');
	      });
	  }
	  
	  if (tableType === "userStats") {
		let restuarantId = localStorage.getItem("restaurantIdAdmin");
	    fetch(`/customer-stats/${restaurantId}`)
	      .then(response => {
	        if (!response.ok) {
	          throw new Error(`HTTP error! Status: ${response.status}`);
	        }
	        return response.json();
	      })
		  .then(data => {
		                         const tbody = document.querySelector('.user-stats-table tbody');
		                         tbody.innerHTML = '';

		                         data.forEach((userStat,index) => {
		                             const row = document.createElement('tr');
		                             row.setAttribute('data-row', JSON.stringify([
		                                 userStat.totalOrders,
		                                 userStat.ordersInLast7Days,
		                                 userStat.ordersInLast30Days,
		                                 userStat.ordersInLast365Days,
		                                 userStat.avgOrdersPerWeek,
		                                 userStat.avgOrdersPerMonth,
		                                 userStat.avgOrdersPerYear
		                             ]));

		                             row.innerHTML = `
		                                 <td>${index+1}</td>
		                                 <td>${userStat.totalOrders}</td>
		                                 <td>${userStat.ordersInLast7Days}</td>
		                                 <td>${userStat.ordersInLast30Days}</td>
		                                 <td>${userStat.ordersInLast365Days}</td>
		                                 <td>${userStat.avgOrdersPerWeek}</td>
		                                 <td>${userStat.avgOrdersPerMonth}</td>
		                                 <td>${userStat.avgOrdersPerYear}</td>
		                             `;
		                             tbody.appendChild(row);
		                         });

		                         addRowClickEvents();
		                     })

	      .catch(error => {
	        console.error('Error fetching user statistics data:', error);
	        alert('Failed to load user statistics table.');
	      });
	  }
	  
	  if (tableType === "dailyReport") {
	          fetch(`/get-daily-report/${restaurantId}`)
	              .then(response => {
	                  if (!response.ok) {
	                      throw new Error(`HTTP error! Status: ${response.status}`);
	                  }
	                  return response.json();
	              })
	              .then(data => {
	                  const tbody = document.querySelector('.daily-report-table tbody');
	                  tbody.innerHTML = '';
	                  data.forEach((report, index) => {
	                      const row = document.createElement('tr');
	                      row.innerHTML = `
	                          <td>${report.reportDate}</td>
	                          <td>${report.totalOrders}</td>
	                          <td>${report.totalOrderValue.toFixed(2)}</td>
	                          <td>${report.completedOrders}</td>
	                          <td>${report.pendingOrders}</td>
	                          <td>${report.canceledOrders}</td>
	                      `;
	                      tbody.appendChild(row);
	                  });
	              })
	              .catch(error => {
	                  console.error('Error fetching daily report data:', error);
	                  alert('Failed to load daily report table.');
	              });
	      }
		  if (tableType === "weeklyReport") {
		            fetch(`/get-weekly-report/${restaurantId}`)
		                .then(response => {
		                    if (!response.ok) {
		                        throw new Error(`HTTP error! Status: ${response.status}`);
		                    }
		                    return response.json();
		                })
		                .then(data => {
		                    const tbody = document.querySelector('.daily-report-table tbody');
		                    tbody.innerHTML = '';
		                    data.forEach((report, index) => {
		                        const row = document.createElement('tr');
		                        row.innerHTML = `
		                            <td>${report.year}</td>
		                            <td>${report.weekNumber}</td>
									<td>${report.totalOrders}</td>
		                            <td>${report.totalOrderValue.toFixed(2)}</td>
		                            <td>${report.completedOrders}</td>
		                            <td>${report.pendingOrders}</td>
		                            <td>${report.canceledOrders}</td>
		                        `;
		                        tbody.appendChild(row);
		                    });
		                })
		                .catch(error => {
		                    console.error('Error fetching daily report data:', error);
		                    alert('Failed to load daily report table.');
		                });
		        }
				if (tableType === "monthlyReport") {
				          fetch(`/get-monthly-report/${restaurantId}`)
				              .then(response => {
				                  if (!response.ok) {
				                      throw new Error(`HTTP error! Status: ${response.status}`);
				                  }
				                  return response.json();
				              })
				              .then(data => {
				                  const tbody = document.querySelector('.daily-report-table tbody');
				                  tbody.innerHTML = '';
				                  data.forEach((report, index) => {
				                      const row = document.createElement('tr');
				                      row.innerHTML = `
										  <td>${report.year}</td>
		  	                          	  <td>${report.month}</td>
		  								  <td>${report.totalOrders}</td>
				                          <td>${report.totalOrderValue.toFixed(2)}</td>
				                          <td>${report.completedOrders}</td>
				                          <td>${report.pendingOrders}</td>
				                          <td>${report.canceledOrders}</td>
				                      `;
				                      tbody.appendChild(row);
				                  });
				              })
				              .catch(error => {
				                  console.error('Error fetching daily report data:', error);
				                  alert('Failed to load daily report table.');
				              });
				      }
					  
					  if (tableType === "menuItemsTable") {
					      fetch(`/get-menu-items/${restaurantId}`, {
					          method: "GET",
					          headers: {
					              "Content-Type": "application/json",
					          },
					      })
					          .then((response) => {
					              if (response.status === 204) {
					                  // No content returned
					                  alert("No menu items found for this restaurant.");
					                  return Promise.reject("No content");
					              }

					              if (!response.ok) {
					                  throw new Error(`Failed to fetch menu items. Status: ${response.status}`);
					              }

					              return response.json(); // Parse JSON response
					          })
					          .then((menuItems) => {
					              // Select the table body and clear existing rows
					              const tableBody = document.querySelector(".menu-items-table tbody");
					              tableBody.innerHTML = "";

					              // Populate the table with menu items
					              menuItems.forEach((item, index) => {
					                  const row = document.createElement("tr");

					                  row.innerHTML = `
					                      <td>${index + 1}</td> <!-- 1-based index -->
					                      <td>${item.name}</td>
					                      <td>${item.description}</td>
					                      <td>${item.price}</td>
					                      <td>${item.stock}</td>
					                      <td>
					                          <!-- Hidden field to store the original itemId -->
					                          <input type="hidden" class="original-id" value="${item.itemId}">
					                          <button class="edit-button" data-id="${item.itemId}">Edit</button>
					                          <button class="delete-button" data-id="${item.itemId}">Delete</button>
					                      </td>
					                  `;

					                  tableBody.appendChild(row);
					              });

					              // Add event listener for delete buttons
					              document.querySelectorAll(".delete-button").forEach((button) => {
					                  button.addEventListener("click", (event) => {
					                      const itemId = event.target.getAttribute("data-id");

					                      // Confirm deletion with the user
					                      const confirmDelete = confirm("Are you sure you want to delete this menu item?");
					                      if (!confirmDelete) return;

					                      // Make a POST request to delete the menu item
					                      fetch(`/delete-menu-item/${itemId}`, {
					                          method: "DELETE",
					                          headers: {
					                              "Content-Type": "application/json",
					                          },
					                      })
					                          .then((response) => {
					                              if (response.status === 204) {
					                                  alert("Menu item deleted successfully.");
					                                  // Remove the row from the table
					                                  event.target.closest("tr").remove();
					                              } else if (response.status === 404) {
					                                  alert("Menu item not found. Deletion failed.");
					                              } else {
					                                  throw new Error("Failed to delete menu item.");
					                              }
					                          })
					                          .catch((error) => {
					                              console.error("Error deleting menu item:", error);
					                              alert("An error occurred while deleting the menu item.");
					                          });
					                  });
					              });
								  
								  document.querySelectorAll(".edit-button").forEach((button) => {
								          button.addEventListener("click", (event) => {
								              const itemId = event.target.getAttribute("data-id");

								              // Fetch details for the selected item
								              fetch(`/get-menu-item/${itemId}`, {
								                  method: "GET",
								                  headers: {
								                      "Content-Type": "application/json",
								                  },
								              })
								                  .then((response) => {
								                      if (!response.ok) {
								                          throw new Error("Failed to fetch menu item details.");
								                      }
								                      return response.json();
								                  })
								                  .then((menuItem) => {
								                      // Create the Add/Edit Menu Item form dynamically
								                   
								                      let formHTML = `<div class="form-container">
								                          <h2>Edit Menu Item</h2>
								                          <form id="edit-menu-form">
								                              <input type="hidden" id="menu-item-id" value="${menuItem.itemId}">
								                              <div>
								                                  <label for="menu-item-name">Name:</label>
								                                  <input type="text" id="menu-item-name" value="${menuItem.name}" required>
								                              </div>
								                              <div>
								                                  <label for="menu-item-description">Description:</label>
								                                  <textarea id="menu-item-description" required>${menuItem.description}</textarea>
								                              </div>
								                              <div>
								                                  <label for="menu-item-price">Price:</label>
								                                  <input type="number" id="menu-item-price" value="${menuItem.price}" required>
								                              </div>
								                              <div>
								                                  <label for="menu-item-stock">Stock:</label>
								                                  <input type="number" id="menu-item-stock" value="${menuItem.stock}" required>
								                              </div>
															  <div>
  								                                  <input type="hidden" id="menu-item-restaurantId" value="${menuItem.restaurantId}">
																  <input type="hidden" id="menu-item-images" value="${menuItem.images}">
																  																  
  								                              </div>
								                              <button type="submit" class="submit">Edit</button>
								                              
								                          </form>
														  </div>
								                      `;
													  displayArea.innerHTML = formHTML;

								                      // Handle form submission for updates
								                      document.getElementById("edit-menu-form").addEventListener("submit", (e) => {
								                          e.preventDefault();

								                          const updatedItem = {
								                              name: document.getElementById("menu-item-name").value,
								                              description: document.getElementById("menu-item-description").value,
								                              price: parseFloat(document.getElementById("menu-item-price").value),
								                              stock: parseInt(document.getElementById("menu-item-stock").value, 10),
															  images:document.getElementById("menu-item-images").value,
															  restaurantId:document.getElementById("menu-item-restaurantId").value,
															  
								                          };

								                          fetch(`/edit-menu-item/${menuItem.itemId}`, {
								                              method: "POST",
								                              headers: {
								                                  "Content-Type": "application/json",
								                              },
								                              body: JSON.stringify(updatedItem),
								                          })
								                              .then((response) => {
								                                  if (response.ok) {
								                                      alert("Menu item updated successfully.");
								                                      location.reload(); // Reload to refresh the table
								                                  } else if (response.status === 404) {
								                                      alert("Menu item not found. Update failed.");
								                                  } else {
								                                      throw new Error("Failed to update menu item.");
								                                  }
								                              })
								                              .catch((error) => {
								                                  console.error("Error updating menu item:", error);
								                                  alert("An error occurred while updating the menu item.");
								                              });
								                      });

								                      // Handle cancel button click
								                      
								                  })
								                  .catch((error) => {
								                      console.error("Error fetching menu item details:", error);
								                      alert("Failed to load menu item details. Please try again.");
								                  });
								          });
								      });

					          })
					          .catch((error) => {
					              if (error !== "No content") {
					                  console.error("Error loading menu items:", error);
					                  alert("An error occurred while loading menu items. Please try again.");
					              }
					          });
					  }
					  
					  if (tableType == "orders") {
					      const orderStatusFilter = document.getElementById('orderStatusFilter');
					      
					      if (orderStatusFilter) {
					          orderStatusFilter.addEventListener('change', function () {
					              const selectedStatus = this.value;

					              // Call your handler method for filtering orders based on status
					              fetchOrdersByStatus(selectedStatus);
					          });
					      } else {
					          console.error('Order status filter not found in the DOM.');
					      }

					      fetchOrdersByStatus("waiting"); // Default to waiting status on load
					  }

					if(tableType == "preferences")
						{
							const chartData = [];
							fetch(`/get-customer-preferences/${restaurantId}`)
							        .then(response => {
							            if (!response.ok) {
							                throw new Error(`HTTP error! Status: ${response.status}`);
							            }
							            return response.json();
							        })
							        .then(data => {
							            const tbody = document.querySelector('.customer-preferences-table tbody');
							            tbody.innerHTML = ''; // Clear existing rows
							            data.forEach(preference => {
							                const row = document.createElement('tr');
							                row.innerHTML = `
							                    <td>${preference.dishName || 'N/A'}</td>
							                    <td>${preference.orderCount || 0}</td>
							                    <td>${preference.customizations?.join(', ') || 'None'}</td>
							                    <td>${preference.percentageOfTotalOrders.toFixed(2) || 0}%</td>
							                `;
							                tbody.appendChild(row);
											chartData.push({
											                    dishName: preference.dishName || 'N/A',
											                    percentage: preference.percentageOfTotalOrders || 0
											                });
							            });
										createPieChartCanvas();
										generatePieChart(chartData);
							        })
							        .catch(error => {
							            console.error('Error fetching customer menu preferences:', error);
							            alert('Failed to load customer preferences table.');
							        });
						}

  }


  async function handleAdminFormSubmit(event) {
    event.preventDefault();

    const restaurantId = localStorage.getItem("restaurantIdAdmin");
    if (!restaurantId) {
      alert("Restaurant ID not found. Please register the restaurant first.");
      return;
    }

    const email = document.getElementById("email").value.trim();
    const username = document.getElementById("username").value.trim();
    const password = document.getElementById("password").value;

    const adminData = {
      restaurantId: parseInt(restaurantId, 10),
      email,
      username,
      password,
    };

    try {
      const response = await fetch("/add-admin", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(adminData),
      });

      if (!response.ok) {
        throw new Error(`HTTP error! Status: ${response.status}`);
      }

      const result = await response.text();

      if (result === "Admin added successfully.") {
        alert("Admin registered successfully!");
        document.getElementById("adminForm").reset();
      } else {
        alert("Failed to register admin. Please try again.");
      }
    } catch (error) {
      console.error("Error registering admin:", error);
      alert("An error occurred. Please try again later.");
    }
  }
});

async function handleMenuItemFormSubmit(event) {
    event.preventDefault();
	const form = event.target;
    // Collect form input values
    const name = document.querySelector('input#name').value;
    const price = document.querySelector('input#price').value;
    const stock = document.querySelector('input#stock').value;
    const description = document.querySelector('textarea#description').value;
    const images = document.querySelector('input#images').files;
    const restaurantId = document.querySelector('input#restaurantId').value;

    // Validate form inputs
    if (!name || !price || !stock || !description || !restaurantId) {
        alert("Please fill in all required fields.");
        return;
    }

    if (!images.length) {
        alert("Please upload at least one image.");
        return;
    }

    // Create the FormData object
    const formData = new FormData();
    formData.append("name", name);
    formData.append("price", price);
    formData.append("stock", stock);
    formData.append("description", description);
    formData.append("restaurantId", restaurantId);

	Array.from(images).forEach((image, index) => {
	    formData.append(`images`, image);
	});

    try {
        // Submit the form data using fetch
        const response = await fetch("/add-menu-item", {
            method: "POST",
            body: formData,
        });

        const result = await response.text();
		
        if (response.ok) {
            alert("Menu item added successfully");
			form.reset();
        } else {
            alert("Error: " + result);
        }
    } catch (error) {
        console.error("Submission error:", error);
        alert("An unexpected error occurred.");
    }
}


function fetchOrdersByStatus(status) {
	let restaurantId = localStorage.getItem("restaurantIdAdmin");
    const url =  `/get-order-items/${restaurantId}`;
        

    fetch(url)
        .then(response => {
            if (!response.ok) {
                throw new Error(`HTTP error! Status: ${response.status}`);
            }
            return response.json();
        })
        .then(data => {
            const tbody = document.querySelector('.order-items-table tbody');
            tbody.innerHTML = ''; // Clear existing rows
			if (status == "waiting") {
			    let i = 1;
			    data.forEach((order) => {
			        if (order.currStatus === "placed") {
			            const row = document.createElement('tr');
			            row.innerHTML = `
			                <td>${i}</td>
			                <td>${order.itemName}</td>
			                <td>${order.quantity}</td>
			                <td>${order.specialRequest || 'None'}</td>
			                <td>${order.price.toFixed(2)}</td>
			                <td>
			                    <!-- Hidden fields to store additional data -->
			                    <input type="hidden" class="order-id" value="${order.orderId}">
			                    <input type="hidden" class="item-id" value="${order.itemId}">
			                    <input type="hidden" class="restaurant-id" value="${order.restaurantId}">
			                    <input type="hidden" class="quantity" value="${order.quantity}">
			                    <input type="hidden" class="price" value="${order.price}">
			                    <input type="hidden" class="specialRequest" value="${order.specialRequest || ''}">
			                    
			                    <button class="accept-button" data-id="${order.id}">Accept</button>
			                </td>
			            `;
			            tbody.appendChild(row);
			            i++;
			        }
			    });

			    // Add event listeners for "Accept" buttons
			    document.querySelectorAll('.accept-button').forEach(button => {
			        button.addEventListener('click', function () {
			            const row = this.closest('tr');
			            const updatedOrder = {
			                id: parseInt(this.dataset.id),
			                orderId: parseInt(row.querySelector('.order-id').value),
			                itemId: parseInt(row.querySelector('.item-id').value),
			                restaurantId: parseInt(row.querySelector('.restaurant-id').value),
			                quantity: parseInt(row.querySelector('.quantity').value),
			                price: parseFloat(row.querySelector('.price').value),
			                specialRequest: row.querySelector('.specialRequest').value,
			                currStatus: "accept"
			            };

			            fetch('/update-order-status', {
			                method: 'PUT',
			                headers: {
			                    'Content-Type': 'application/json'
			                },
			                body: JSON.stringify(updatedOrder)
			            })
			            .then(response => {
			                if (!response.ok) {
			                    throw new Error(`HTTP error! Status: ${response.status}`);
			                }
			                return response.text();
			            })
			            .then(message => {
			                row.remove(); // Remove the row from the table
			            })
			            .catch(error => {
			                console.error('Error updating order status:', error);
			                alert('Failed to update order status. Please try again.');
			            });
			        });
			    });
			}

			else if (status == "preparing") {
			    let i = 1;
			    data.forEach((order) => {
			        if (order.currStatus === "accept") {
			            const row = document.createElement('tr');
			            row.innerHTML = `
			                <td>${i}</td>
			                <td>${order.itemName}</td>
			                <td>${order.quantity}</td>
			                <td>${order.specialRequest || 'None'}</td>
			                <td>${order.price.toFixed(2)}</td>
			                <td>
			                    <!-- Hidden fields to store additional data -->
			                    <input type="hidden" class="order-id" value="${order.orderId}">
			                    <input type="hidden" class="item-id" value="${order.itemId}">
			                    <input type="hidden" class="restaurant-id" value="${order.restaurantId}">
			                    <input type="hidden" class="quantity" value="${order.quantity}">
			                    <input type="hidden" class="price" value="${order.price}">
			                    <input type="hidden" class="specialRequest" value="${order.specialRequest || ''}">
			                    
			                    <button class="dispatch-button" data-id="${order.id}">Send to Dispatch</button>
			                </td>
			            `;
			            tbody.appendChild(row);
			            i++;
			        }
			    });

			    // Add event listeners for "Send to Dispatch" buttons
			    document.querySelectorAll('.dispatch-button').forEach(button => {
			        button.addEventListener('click', function () {
			            const row = this.closest('tr');
			            const updatedOrder = {
			                id: parseInt(this.dataset.id),
			                orderId: parseInt(row.querySelector('.order-id').value),
			                itemId: parseInt(row.querySelector('.item-id').value),
			                restaurantId: parseInt(row.querySelector('.restaurant-id').value),
			                quantity: parseInt(row.querySelector('.quantity').value),
			                price: parseFloat(row.querySelector('.price').value),
			                specialRequest: row.querySelector('.specialRequest').value,
			                currStatus: "dispatched"
			            };

			            fetch('/update-order-status', {
			                method: 'PUT',
			                headers: {
			                    'Content-Type': 'application/json'
			                },
			                body: JSON.stringify(updatedOrder)
			            })
			            .then(response => {
			                if (!response.ok) {
			                    throw new Error(`HTTP error! Status: ${response.status}`);
			                }
			                return response.text();
			            })
			            .then(message => {
			                row.remove(); // Remove the row from the table
			            })
			            .catch(error => {
			                console.error('Error updating order status:', error);
			                alert('Failed to update order status. Please try again.');
			            });
			        });
			    });
			}

			else if(status == "dispatched")
				{
					const tableHeaderRow = document.querySelector('.order-items-table thead tr');
					    if (tableHeaderRow) {
					        const lastHeaderCell = tableHeaderRow.querySelector('th:last-child');
					        if (lastHeaderCell) {
					            lastHeaderCell.remove();
					        } else {
					            console.error('No last header cell found.');
					        }
					    } else {
					        console.error('Table header row not found.');
					    }
					let i = 1;
					data.forEach((order, index) => {
						
						if(order.currStatus == "dispatched"){
							const row = document.createElement('tr');
						                row.innerHTML = `
						                    <td>${i}</td>
						                    <td>${order.itemName}</td>
						                    <td>${order.quantity}</td>
						                    <td>${order.specialRequest || 'None'}</td>
						                    <td>${order.price.toFixed(2)}</td>
						                `;
						                tbody.appendChild(row);
										i = i+1;
						}
						            
					     });
				}
			
            
        })
        .catch(error => {
            console.error('Error fetching orders:', error);
            alert('Failed to load orders.');
        });
}

async function fetchDashboardData(restaurantId) {
  try {
    const response = await fetch(`get-dashboard/${restaurantId}`, {
      method: "GET",
      headers: {
        "Content-Type": "application/json",
      },
    });

    if (response.ok) {
      const dashboardData = await response.json();
      updateDashboardContent(dashboardData);
    } else {
      console.error(`Failed to fetch dashboard data: ${response.status}`);
    }
  } catch (error) {
    console.error("Error fetching dashboard data:", error);
  }
}

function updateDashboardContent(data) {
  const formattedPrice = new Intl.NumberFormat('en-US', {
    style: 'currency',
    currency: 'INR', // Change 'USD' to your desired currency code
  }).format(data.totalPrice);

  let username = localStorage.getItem("usernameAdmin");
  username = username.charAt(0).toUpperCase() + username.slice(1);

  const initialContent = `
    <div class="dashboard-container">
      <h1 class="welcome-message" style="font-size:44px;">👋 Welcome! ${username}</h1>
      <div class="cards-container">
        <div class="card">
          <div class="card-icon file-icon"><i class="fa-solid fa-user"></i></div>
          <div class="card-content">
            <h3>CUSTOMERS</h3>
            <p>${data.userCount}</p>
          </div>
        </div>
        <div class="card">
          <div class="card-icon calendar-icon"><i class="fa-solid fa-bars"></i></div>
          <div class="card-content">
            <h3>MENUITEMS</h3>
            <p>${data.menuItems}</p>
          </div>
        </div>
        <div class="card">
          <div class="card-icon delivery-icon"><i class="fa-solid fa-star"></i></div>
          <div class="card-content">
            <h3>RATING</h3>
            <p>${data.avgRating}</p>
          </div>
        </div>
        <div class="card">
          <div class="card-icon category-icon"><i class="fa-solid fa-money-bill-wave"></i></div>
          <div class="card-content">
            <h3>REVENUE</h3>
            <p>${formattedPrice}</p>
          </div>
        </div>
      </div>
      <div class="location-container">
        <br>
        <div id="map" style="width: 100%; height: 300px;"></div>
      </div>
    </div>
  `;

  const logoElement = document.getElementsByClassName("logo")[0];
  if (logoElement) {
    logoElement.textContent = data.restaurantName; // Set the restaurant name
  }

  const displayArea = document.getElementById("displayArea");
  if (displayArea) {
    displayArea.innerHTML = initialContent;
    initGoogleMap(data.location); // Call the function to initialize the map
  } else {
    console.error("Dashboard container not found.");
  }
}

function initGoogleMap(location) {
  const mapElement = document.getElementById("map");
  if (!mapElement) {
    console.error("Map container not found.");
    return;
  }

  // Parse the location string into latitude and longitude
  const [lat, lng] = location.split(',').map(coord => parseFloat(coord.trim()));

  if (isNaN(lat) || isNaN(lng)) {
    console.error("Invalid location data:", location);
    return;
  }

  const position = { lat, lng };

  // Initialize the map
  const map = new google.maps.Map(mapElement, {
    center: position,
    zoom: 15,
  });

  // Add a marker for the location with a custom icon
  new google.maps.Marker({
    position,
    map,
    title: `Location: ${lat}, ${lng}`,
    icon: {
      url: "https://maps.google.com/mapfiles/ms/icons/red-dot.png", // URL for the custom marker icon
      scaledSize: new google.maps.Size(40, 40), // Adjust the size as needed
    },
  });
}


function generatePieChart(data) {
    // Assume you're using a chart library like Chart.js or any other for generating the pie chart
    const labels = data.map(item => item.dishName);
    const percentages = data.map(item => item.percentage);

    const ctx = document.getElementById('pieChart').getContext('2d');
    new Chart(ctx, {
        type: 'pie',
        data: {
            labels: labels,
            datasets: [{
                label: 'Customer Preferences',
                data: percentages,
                backgroundColor: [
                    '#ff9999', '#66b3ff', '#99ff99', '#ffcc99', '#c2c2f0', '#ffb3e6',
                    '#c2f0c2', '#ffb366', '#99ccff', '#ff6666', '#ffccff', '#ffff99',
                    '#ff99cc', '#ffcc33', '#33cc33', '#ccccff', '#ffb3cc', '#99ffcc',
                    '#b3b3cc', '#ff6666'
                ], // Customize colors
            }]
        },
        options: {
            responsive: true,
            plugins: {
                legend: {
                    position: 'top',
                    labels: {
                        font: {
                            size: 16, // Increase label font size
                        }
                    }
                },
                tooltip: {
                    callbacks: {
                        label: function(tooltipItem) {
                            return tooltipItem.label + ': ' + tooltipItem.raw.toFixed(2) + '%';
                        }
                    }
                },
                datalabels: {
                    anchor: 'end', // Position the label outside
                    align: 'start', // Align the label to the outside of the pie
                    font: {
                        size: 14, // Font size for the labels outside the pie
                    },
                    formatter: (value, context) => {
                        return context.chart.data.labels[context.dataIndex] + ': ' + value.toFixed(2) + '%';
                    }
                }
            }
        }
    });
}
function createPieChartCanvas() {
    // Check if a canvas element with id "pieChart" already exists
    let existingCanvas = document.getElementById('pieChart');
    if (existingCanvas) {
        existingCanvas.remove(); // Remove the existing canvas if present
    }

    // Create a new canvas element
    const canvas = document.createElement('canvas');
    canvas.id = 'pieChart'; // Set the id for the canvas

    // Add styles for centering
    canvas.style.display = 'block';
    canvas.style.margin = '0 auto';

    // Append the canvas to the main-content element
    const mainContent = document.getElementsByClassName("main-content")[0];
    if (mainContent) {
        mainContent.appendChild(canvas);
    } else {
        console.error('No element with class "main-content" found.');
    }
}
function addRowClickEvents() {
    const tableRows = document.querySelectorAll('.user-stats-table tbody tr');

    let barChart; // Define the chart variable outside to reuse it

    tableRows.forEach(row => {
        row.addEventListener('click', function () {
            const rowData = JSON.parse(this.getAttribute('data-row'));

            // Remove any existing modal to ensure a clean slate
            const existingModal = document.querySelector('#dynamicModal');
            if (existingModal) {
                existingModal.remove();
            }

            // Create the modal dynamically
            const modal = document.createElement('div');
            modal.id = 'dynamicModal';
            modal.className = 'modal';
            modal.style.display = 'flex'; // Show the modal

            modal.innerHTML = `
                <div class="modal-content">
                    <span class="close">&times;</span>
                    <canvas id="dynamicBarChart"></canvas>
                </div>
            `;

            document.body.appendChild(modal); // Append the modal to the document body

            // Close modal on clicking the close button
            modal.querySelector('.close').addEventListener('click', () => {
                modal.style.display = 'none';
                if (barChart) {
                    barChart.destroy(); // Destroy the chart when modal closes
                }
                modal.remove(); // Remove modal from DOM
            });

            // Close modal on clicking outside the content
            modal.addEventListener('click', (event) => {
                if (event.target === modal) {
                    modal.style.display = 'none';
                    if (barChart) {
                        barChart.destroy(); // Destroy the chart
                    }
                    modal.remove(); // Remove modal from DOM
                }
            });

            // Get the canvas element for the chart
            const barChartCanvas = document.getElementById('dynamicBarChart').getContext('2d');

            // Destroy existing chart instance if it exists
            if (barChart) {
                barChart.destroy();
            }

            // Create a new bar chart
            barChart = new Chart(barChartCanvas, {
                type: 'bar',
                data: {
                    labels: [
                        'Total Orders',
                        'Orders in Last 7 Days',
                        'Orders in Last 30 Days',
                        'Orders in Last 365 Days',
                        'Avg Orders Per Week',
                        'Avg Orders Per Month',
                        'Avg Orders Per Year'
                    ],
                    datasets: [{
                        label: 'User Data',
                        data: rowData,
						backgroundColor: [
						    '#FFC1C1', 
						    '#9CC8ED', 
						    '#AAE2C2', 
						    '#D2C0E0', 
						    '#F4E8AB', 
						    '#D0F0C0', 
						    '#E2B6CD'  
						],

                        borderColor: '#333',
                        borderWidth: 1
                    }]
                },
                options: {
                    responsive: true,
                    plugins: {
                        legend: {
                            display: false
                        }
                    },
                    scales: {
                        y: {
                            beginAtZero: true
                        }
                    }
                }
            });
        });
    });
}

function syncSidebarHeight() {
  const sidebar = document.querySelector(".sidebar");
  const mainContent = document.querySelector(".main-content");

  // Get the heights of sidebar and main-content
  const sidebarHeight = sidebar.offsetHeight;
  const mainContentHeight = mainContent.offsetHeight;

  // Adjust the sidebar height if main-content is taller
  if (mainContentHeight > sidebarHeight) {
    sidebar.style.height = `${mainContentHeight}px`;
  } else {
    sidebar.style.height = ""; // Reset to default (let CSS handle it)
  }
}

// Call function on load and whenever content dynamically changes
window.addEventListener("load", syncSidebarHeight);
window.addEventListener("resize", syncSidebarHeight);

// Optional: Call whenever dynamic content is added
const displayArea = document.getElementById("displayArea");
const observer = new MutationObserver(syncSidebarHeight);
observer.observe(displayArea, { childList: true, subtree: true });
