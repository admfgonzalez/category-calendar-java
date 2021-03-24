var categoryCalendarApp = (function () {
  let loadedCategoriesById = {};
  let loadedCategorySchedulesByCategory = {};
  let selectedCategoryBtnId = null;
  let lastChangedtimeCategory = null;
  let lastChangedtimeCategorySchedule = null;

  const serverAndPort = 'localhost:8080';

  function initPoll() {
    clearNewCategoryForm();
    ajaxLastChangedTimeCategory();
    setInterval(function () {
      ajaxLastChangedTimeCategory();
      ajaxLastChangedTimeCategorySchedule();
    }, 15000);
  }

  function loadCategoriesInView(responseCategories) {
    clearCategoriesGrid();
    responseCategories.forEach(addCategoryButton);
    checkCategoryBtnSelected();
  }

  function loadCategoryScheduleDay(categorySchedule) {
    let categoryId = categorySchedule.category.id;
    
    if (typeof loadedCategorySchedulesByCategory[categoryId] === 'undefined') {
      loadedCategorySchedulesByCategory[categoryId] = {};
    }

    loadedCategorySchedulesByCategory[categoryId][
      categorySchedule.scheduleDate
    ] = categorySchedule;
    reloadCategoryCount(categoryId);

    if (categorySchedule.active) {
      $('#day-content-' + categorySchedule.scheduleDate).append(
        '<div id="category-schedule-btn-' +
          categorySchedule.id +
          '" class="category-schedule" style="background-color: ' +
          categorySchedule.category.color +
          ';">' +
          categorySchedule.category.name.charAt(0).toUpperCase() +
          '</div>'
      );
    }
  }

  function reloadCategoryCount(categoryId) {
    let categoryInView = loadedCategoriesById[categoryId];
    $('#category-count-' + categoryInView.id).html(countCategoryScheduleActive(loadedCategorySchedulesByCategory[categoryId]));
  }

  function clearCategoriesGrid() {
    $('#categories-table').html('');
  }

  function clearCategorySchedulesGrid() {
    $('.day-content').html('');
  }

  function addCategoryButton(category, index) {
    let oldCategory = loadedCategoriesById[category.id];
    if (typeof oldCategory !== 'undefined' && oldCategory !== null) {
      removeCategoryBtn(category.id);
      let oldCategorySchedulesMap = loadedCategorySchedulesByCategory[category.id];
      if (
        category.active &&
        typeof oldCategorySchedulesMap !== 'undefined' &&
        oldCategorySchedulesMap !== null && category.color !== oldCategory.color
      ) {
        changeColorToCategorySchedulesDay(oldCategorySchedulesMap, category);
      }
    }
    loadedCategoriesById[category.id] = category;
    if (category.active) {
      $('#categories-table').append(
        '<div id="category-btn-' +
          category.id +
          '" class="btn btn-info btn-xs category category-btn" ' +
          'style="background-color: ' +
          category.color +
          ';border-color: ' +
          category.color +
          ';" onclick="categoryCalendarApp.selectCategoryBtn(' +
          category.id +
          ')">' +
          '<div data-toggle="tooltip" data-placement="bottom" title="Number of categories scheduled" class="category-count" id="category-count-' + category.id + '">0</div>' +
          category.name +
          '<div data-toggle="tooltip" data-placement="bottom" title="Click to delete" class="delete-me-btn" onclick="categoryCalendarApp.deleteCategoryConfirm(' +
          category.id +
          ')"><i class="fa fa-trash"></i></div>' +
        '</div>'
      );
    }
  }

  function changeColorToCategorySchedulesDay(categorySchedulesMap, category) {
    Object.values(categorySchedulesMap).forEach(function (categorySchedule) {
      changeColorToCategoryScheduleDay(categorySchedule, category);
    });
  }

  function changeColorToCategoryScheduleDay(categorySchedule, category) {
    $('#category-schedule-btn-' + categorySchedule.id).css('background-color', category.color);
  }

  function removeCategoryBtn(categoryId) {
    $('#category-btn-' + categoryId).remove();
  }

  function removeCategorySchedulesDayByCategoryId(categoryId) {
    Object.values(loadedCategorySchedulesByCategory[categoryId]).forEach(
      removeCategorySchedulesDay
    );
  }

  function removeCategorySchedulesDay(categorySchedule) {
    reloadCategoryCount(categorySchedule.category.id);
    $('#category-schedule-btn-' + categorySchedule.id).remove();
  }

  function selectCategoryBtn(categoryBtnId) {
    $('.category-btn').removeClass('selected-category-btn');
    if (
      selectedCategoryBtnId === null ||
      selectedCategoryBtnId !== categoryBtnId
    ) {
      $('#category-btn-' + categoryBtnId).addClass('selected-category-btn');
      selectedCategoryBtnId = categoryBtnId;
    } else {
      selectedCategoryBtnId = null;
    }
  }

  function checkCategoryBtnSelected() {
    if (selectedCategoryBtnId !== null) {
      $('#category-btn-' + selectedCategoryBtnId).addClass('selected-category-btn');
    }
  }

  function clearNewCategoryForm() {
    $('#name').val('');
    $('#categoryColor').val('');
    $('#colorPickerAddon i').css('background-color', 'black');
  }

  function showError(title, message) {
    $('#errorModalTitle').html(title);
    $('#errorModalMessage').html(message);
    $('#errormodal').modal('show');
  }

  function showReloadDataAlert() {
    $('#reloadDataAlert').css('display', 'unset');
    $('#reloadDataAlert').fadeOut(5000, function() {
      $('#reloadDataAlert').css('display', 'none');
    });
  }

  function deleteCategoryConfirm(categoryId) {
    let categoryToDelete = loadedCategoriesById[categoryId];
    if (typeof categoryToDelete !== 'undefined' && categoryToDelete !== null) {
      $('#deleteDescription').html(
        'All schedules of the category <span class="delete-category-confirm-name">"' +
          categoryToDelete.name +
          '"</span> will be removed.'
      );
      $('#deleteMoldelConfirm').attr(
        'onclick',
        'categoryCalendarApp.deleteCategory(' +
          categoryId +
          ');$("#deletemodel").modal("hide")'
      );
      $('#deletemodel').modal('show');
    }
    event.stopPropagation();
  }

  function deleteCategory(categoryId) {
    ajaxRemoveCategory(categoryId);
  }

  function addCategory(name, color) {
    if (
      typeof name === 'undefined' ||
      name == null ||
      name.trim().length == 0 ||
      name.trim().length < 3 ||
      typeof color === 'undefined' ||
      color == null ||
      color.trim().length == 0 ||
      color.trim().length !== 7
    ) {
      showError(
        'Warning',
        'The category must be min three of length  and a color selected.'
      );
    } else {
      ajaxAddCategory({
        name,
        color,
        active: true,
      });
    }
  }

  function checkCategoryDeletedIsSelected(categoryId) {
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
        loadedCategorySchedulesByCategory[selectedCategoryBtnId];
      let categorySearch =
        typeof categorySchedulesBySelectedCategory === 'undefined' ||
        categorySchedulesBySelectedCategory === null
          ? null
          : categorySchedulesBySelectedCategory[categoryScheduleDate];
      if (
        typeof categorySearch !== 'undefined' &&
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
    ajaxAddCategorySchedule({
      scheduleDate: schedule,
      active: true,
      category: loadedCategoriesById[categoryId],
    });
  }

  function deleteCategiorySchedule(categorySchedule) {
    ajaxRemoveCategorySchedule(categorySchedule);
  }

  function evaluateShowReloadDataAlert(oldArray, newArray) {
    const oldArrayString = JSON.stringify(oldArray);
    for (const newElement of newArray) {
      if (!oldArrayString.includes(JSON.stringify(newElement))) {
        showReloadDataAlert();
        break;
      }
    }
  }

  function getFlatCategorySchedules() {
    let result = [];
    Object.keys(loadedCategorySchedulesByCategory).map((categoryKey) => {
      Object.keys(loadedCategorySchedulesByCategory[categoryKey]).map((scheduleKey) => {
        result.push(loadedCategorySchedulesByCategory[categoryKey][scheduleKey]);
      });
    });
    return result;
  }

  function loadCategorySchedulesOnView(newCategorySchedules) {
    newCategorySchedules.forEach(loadCategoryScheduleDay);
    Object.keys(loadedCategorySchedulesByCategory).every(function(key) {
      reloadCategoryCount(key);
    });
  }

  function countCategoryScheduleActive(loadedCategorySchedulesByScheduleDate) {
    return Object.values(loadedCategorySchedulesByScheduleDate).filter(element => element.active).length;
  }

  // Rest functions
  function ajaxLastChangedTimeCategory() {
    $.ajax({
      url: 'http://' + serverAndPort + '/category/getlastchangetime',
      success: function (responsetime) {
        if (
          lastChangedtimeCategory === null ||
          lastChangedtimeCategory !== responsetime
        ) {
          ajaxLoadCategories(lastChangedtimeCategory !== null);
          ajaxGetCurrentYearCategorySchedules(lastChangedtimeCategory !== null);
          lastChangedtimeCategory = responsetime;
        }
      },
      error: function (errorResponse) {
        showError('Error', errorResponse['responseJSON']['message']);
      },
    });
  }

  function ajaxLastChangedTimeCategorySchedule() {
    $.ajax({
      url: 'http://' + serverAndPort + '/categoryschedule/getlastchangetime',
      success: function (responsetime) {
        if (
          lastChangedtimeCategorySchedule === null ||
          lastChangedtimeCategorySchedule !== responsetime
        ) {
          ajaxGetCurrentYearCategorySchedules(lastChangedtimeCategorySchedule !== null);
          lastChangedtimeCategorySchedule = responsetime;
        }
      },
      error: function (errorResponse) {
        showError('Error', errorResponse['responseJSON']['message']);
      },
    });
  }

  function ajaxLoadCategories(evaluateMessage) {
    $.ajax({
      url: 'http://' + serverAndPort + '/category/getcategories',
      success: function (responseCategories) {
        if (evaluateMessage)
          evaluateShowReloadDataAlert(Object.values(loadedCategoriesById), responseCategories);
        loadCategoriesInView(responseCategories);
      },
      error: function (errorResponse) {
        showError('Error', errorResponse['responseJSON']['message']);
      },
    });
  }

  function ajaxAddCategory(category) {
    $.ajax({
      url: 'http://' + serverAndPort + '/category/addcategory/',
      data: JSON.stringify(category),
      type: 'POST',
      contentType: 'application/json',
      success: function (responseCategory) {
        addCategoryButton(responseCategory);
        clearNewCategoryForm();
      },
      error: function (errorResponse) {
        showError('Error', errorResponse['responseJSON']['message']);
      },
    });
  }

  function ajaxRemoveCategory(categoryId) {
    $.ajax({
      url: 'http://' + serverAndPort + '/category/removecategory/',
      data: JSON.stringify(loadedCategoriesById[categoryId]),
      type: 'POST',
      contentType: 'application/json',
      success: function () {
        loadedCategoriesById[categoryId].active = false;
        checkCategoryDeletedIsSelected(categoryId);
        removeCategoryBtn(categoryId);
        removeCategorySchedulesDayByCategoryId(categoryId);
      },
      error: function (errorResponse) {
        showError('Error', errorResponse['responseJSON']['message']);
      },
    });
  }

  function ajaxGetCurrentYearCategorySchedules(evaluateMessage) {
    $.ajax({
      url:
        'http://' +
        serverAndPort +
        '/categoryschedule/getcategoryschedulesbyyear',
      data: {
        year: new Date().getFullYear(),
      },
      contentType: 'application/json',
      success: function (responseCategoriesSchedules) {
        clearCategorySchedulesGrid();
        if (evaluateMessage)
          evaluateShowReloadDataAlert(getFlatCategorySchedules(), responseCategoriesSchedules);
        loadCategorySchedulesOnView(responseCategoriesSchedules);
      },
      error: function (errorResponse) {
        showError('Error', errorResponse['responseJSON']['message']);
      },
    });
  }

  function ajaxRemoveCategorySchedule(categorySchedule) {
    $.ajax({
      url:
        'http://' + serverAndPort + '/categoryschedule/removecategoryschedule',
      data: JSON.stringify(categorySchedule),
      type: 'POST',
      contentType: 'application/json',
      success: function () {
        categorySchedule.active = false;
        removeCategorySchedulesDay(categorySchedule);
      },
      error: function (errorResponse) {
        showError('Error', errorResponse['responseJSON']['message']);
      },
    });
  }

  function ajaxAddCategorySchedule(categorySchedule) {
    $.ajax({
      url: 'http://' + serverAndPort + '/categoryschedule/addcategoryschedule',
      data: JSON.stringify(categorySchedule),
      type: 'POST',
      contentType: 'application/json',
      success: function (responseCategorySchedule) {
        loadCategoryScheduleDay(responseCategorySchedule);
      },
      error: function (errorResponse) {
        showError('Error', errorResponse['responseJSON']['message']);
      },
    });
  }

  return {
    getScheduleId,
    initPoll,
    deleteCategory,
    deleteCategoryConfirm,
    selectCategoryBtn,
    addCategory,
    selectDay
  };
})();
