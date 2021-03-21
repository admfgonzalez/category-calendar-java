var categoryCalendarApp = (function () {
  let loadedCategories = [];
  let loadedCategoriesById = {};
  let loadedCategorySchedulesByDate = {};
  let selectedCategoryBtnId = null;

  // Functions
  function initCalendar() {
    clearNewCategoryForm();
    ajaxLoadCategories();
    ajaxGetCurrentYearCategorySchedules();
  }

  function loadCategories(responseCategories) {
    clearCategories();
    loadedCategories = responseCategories;
    loadedCategories.forEach(addCategoryButton);
    checkCategoryBtnSelected();
  }

  function loadCategorySchedules(categorySchedules) {
    clearCategorySchedules();
    categorySchedules.forEach(loadCategorySchedule);
  }

  function loadCategorySchedule(categorySchedule) {
    let categoryId = categorySchedule.category.id;
    if (
      typeof loadedCategorySchedulesByDate[categoryId] === "undefined" ||
      loadedCategorySchedulesByDate[categoryId] === null
    ) {
      loadedCategorySchedulesByDate[categoryId] = {};
    }
    loadedCategorySchedulesByDate[categoryId][
      categorySchedule.scheduleDate
    ] = categorySchedule;

    if (categorySchedule.active) {
      $("#day-content-" + categorySchedule.scheduleDate).append(
        '<div class="category-schedule" style="background-color: ' +
          categorySchedule.category.color +
          ';">' +
          categorySchedule.category.name.charAt(0).toUpperCase() +
          "</div>"
      );
    }
  }

  function clearCategories() {
    $("#categories-table").html("");
  }

  function clearCategorySchedules() {
    $(".day-content").html("");
  }

  function addCategoryButton(category, index) {
    loadedCategoriesById[category.id] = category;
    if (category.active) {
      $("#categories-table").append(
        '<div id="btn-' +
          category.id +
          '" class="btn btn-info btn-xs category category-btn" ' +
          'style="background-color: ' +
          category.color +
          ";border-color: " +
          category.color +
          ';" onclick="categoryCalendarApp.selectCategoryBtn(' +
          category.id +
          ')">' +
          category.name +
          '<div class="delete-me-btn" onclick="categoryCalendarApp.deleteCategoryConfirm(' +
          category.id +
          ')"><i class="fa fa-trash"></i></div>' +
          "</div>"
      );
    }
  }

  function selectCategoryBtn(categoryBtnId) {
    $(".category-btn").removeClass("selected-category-btn");
    if (
      selectedCategoryBtnId === null ||
      selectedCategoryBtnId !== categoryBtnId
    ) {
      $("#btn-" + categoryBtnId).addClass("selected-category-btn");
      selectedCategoryBtnId = categoryBtnId;
    } else {
      selectedCategoryBtnId = null;
    }
  }

  function checkCategoryBtnSelected() {
    if (selectedCategoryBtnId !== null) {
      $("#btn-" + selectedCategoryBtnId).addClass("selected-category-btn");
    }
  }

  function deleteCategoryConfirm(categoryId) {
    let categoryToDelete = loadedCategoriesById[categoryId];
    if (typeof categoryToDelete !== "undefined" && categoryToDelete !== null) {
      $("#deleteDescription").html(
        'All schedules of the category <span class="delete-category-confirm-name">"' +
          categoryToDelete.name +
          '"</span> will be removed.'
      );
      $("#deleteMoldelConfirm").attr(
        "onclick",
        "categoryCalendarApp.deleteCategory(" + categoryId + ');$("#deletemodel").modal("hide")'
      );
      $("#deletemodel").modal("show");
    }
    event.stopPropagation();
  }

  function deleteCategory(categoryId) {
    ajaxRemoveCategory(categoryId);
  }

  function addCategory(name, color) {
    if (
      typeof name === "undefined" ||
      name == null ||
      name.trim().length == 0 ||
      name.trim().length < 3 ||
      typeof color === "undefined" ||
      color == null ||
      color.trim().length == 0 ||
      color.trim().length !== 7
    ) {
      showError('Warning', 'The category must be min large of three, and must be select a color.');
    } else {
      ajaxAddCategory({
        name,
        color,
        active: true,
      });
    }
  }

  function clearNewCategoryForm() {
    $("#name").val("");
    $("#color").val("");
    $("#colorPickerAddon i").css("background-color", "black");
  }

  function checkCategoryDeleted(categoryId) {
    if (categoryId === selectedCategoryBtnId) {
      selectedCategoryBtnId === null;
    }
  }

  function getScheduleId(year, month, day) {
    return year * 10000 + month * 100 + day;
  }

  function selectDay(categoryScheduleDate) {
    if (selectedCategoryBtnId !== null) {
      let categorySchedulesBySelectedCategory =
        loadedCategorySchedulesByDate[selectedCategoryBtnId];
      let categorySearch =
        typeof categorySchedulesBySelectedCategory === "undefined" ||
        categorySchedulesBySelectedCategory === null
          ? null
          : categorySchedulesBySelectedCategory[categoryScheduleDate];
      if (
        typeof categorySearch !== "undefined" &&
        categorySearch !== null &&
        categorySearch.active
      ) {
        deleteCategiorySchedule(categorySearch);
      } else {
        addCategiorySchedule(selectedCategoryBtnId, categoryScheduleDate);
      }
    }
  }

  function addCategiorySchedule(categoryId, schedule) {
    addCategorySchedule({
      scheduleDate: schedule,
      active: true,
      category: loadedCategoriesById[categoryId],
    });
  }

  function deleteCategiorySchedule(categorySchedule) {
    removeCategorySchedule(categorySchedule);
  }

  function showError(title, message) {
    $('#errorModalTitle').html(title);
    $('#errorModalMessage').html(message);
    $("#errormodal").modal("show");
  }

  // Ajax calls
  function ajaxLoadCategories() {
    $.ajax({
      url: "http://localhost:8080/category/getcategories",
      success: function (responseCategories) {
        loadCategories(responseCategories);
      },
      error: function (errorResponse) {
        showError('Error', errorResponse['responseJSON']['message']);
      },
    });
  }

  function ajaxAddCategory(category) {
    $.ajax({
      url: "http://localhost:8080/category/addcategory/",
      data: JSON.stringify(category),
      type: "POST",
      contentType: "application/json",
      success: function (responseCategory) {
        initCalendar();
      },
      error: function (errorResponse) {
        showError('Error', errorResponse['responseJSON']['message']);
      },
    });
  }

  function ajaxRemoveCategory(categoryId) {
    $.ajax({
      url: "http://localhost:8080/category/removecategory/",
      data: JSON.stringify(loadedCategoriesById[categoryId]),
      type: "POST",
      contentType: "application/json",
      success: function (data) {
        checkCategoryDeleted(categoryId);
        ajaxLoadCategories();
        ajaxGetCurrentYearCategorySchedules();
      },
      error: function (errorResponse) {
        showError('Error', errorResponse['responseJSON']['message']);
      },
    });
  }

  function ajaxGetCurrentYearCategorySchedules() {
    $.ajax({
      url: "http://localhost:8080/categoryschedule/getcategoryschedulesbyyear",
      data: {
        year: new Date().getFullYear(),
      },
      contentType: "application/json",
      success: function (responseCategoriesSchedules) {
        loadCategorySchedules(responseCategoriesSchedules);
      },
      error: function (errorResponse) {
        showError('Error', errorResponse['responseJSON']['message']);
      },
    });
  }

  function removeCategorySchedule(categorySchedule) {
    $.ajax({
      url: "http://localhost:8080/categoryschedule/removecategoryschedule",
      data: JSON.stringify(categorySchedule),
      type: "POST",
      contentType: "application/json",
      success: function (response) {
        ajaxGetCurrentYearCategorySchedules();
      },
      error: function (errorResponse) {
        showError('Error', errorResponse['responseJSON']['message']);
      },
    });
  }

  function addCategorySchedule(categorySchedule) {
    $.ajax({
      url: "http://localhost:8080/categoryschedule/addcategoryschedule",
      data: JSON.stringify(categorySchedule),
      type: "POST",
      contentType: "application/json",
      success: function (repsonseCategorySchedule) {
        ajaxGetCurrentYearCategorySchedules();
      },
      error: function (errorResponse) {
        showError('Error', errorResponse['responseJSON']['message']);
      },
    });
  }

  return {
    getScheduleId,
    initCalendar,
    deleteCategory,
    deleteCategoryConfirm,
    selectCategoryBtn,
    addCategory,
    selectDay
  };
})();
