const expenseCategories = ["餐饮", "购物", "教育", "交通", "娱乐"];
const incomeCategories = ["兼职", "生活费", "奖学金"];
const allCategories = [...expenseCategories, ...incomeCategories];

const state = {
    user: null,
    month: "",
    report: null,
    transactions: [],
    budgets: [],
    overBudgets: []
};

const els = {
    authPage: document.querySelector("#authPage"),
    appPage: document.querySelector("#appPage"),
    loginForm: document.querySelector("#loginForm"),
    registerForm: document.querySelector("#registerForm"),
    loginUserText: document.querySelector("#loginUserText"),
    logoutBtn: document.querySelector("#logoutBtn"),
    monthInput: document.querySelector("#monthInput"),
    sideMonth: document.querySelector("#sideMonth"),
    refreshBtn: document.querySelector("#refreshBtn"),
    transactionForm: document.querySelector("#transactionForm"),
    budgetForm: document.querySelector("#budgetForm"),
    budgetFormTitle: document.querySelector("#budgetFormTitle"),
    resetBudgetBtn: document.querySelector("#resetBudgetBtn"),
    categoryFilter: document.querySelector("#categoryFilter"),
    transactionCategoryOptions: document.querySelector("#transactionCategoryOptions"),
    budgetCategoryOptions: document.querySelector("#budgetCategoryOptions"),
    allCategoryOptions: document.querySelector("#allCategoryOptions"),
    totalIncome: document.querySelector("#totalIncome"),
    totalExpense: document.querySelector("#totalExpense"),
    balance: document.querySelector("#balance"),
    heroBalance: document.querySelector("#heroBalance"),
    overCount: document.querySelector("#overCount"),
    recentCount: document.querySelector("#recentCount"),
    budgetCount: document.querySelector("#budgetCount"),
    overHint: document.querySelector("#overHint"),
    categoryStats: document.querySelector("#categoryStats"),
    recentList: document.querySelector("#recentList"),
    transactionTable: document.querySelector("#transactionTable"),
    budgetList: document.querySelector("#budgetList"),
    overBudgetList: document.querySelector("#overBudgetList"),
    dailyTrend: document.querySelector("#dailyTrend"),
    sparkline: document.querySelector("#sparkline"),
    toast: document.querySelector("#toast")
};

function today() {
    return new Date().toISOString().slice(0, 10);
}

function currentMonth() {
    return today().slice(0, 7);
}

function money(value) {
    return `¥${Number(value || 0).toFixed(2)}`;
}

async function api(url, options = {}) {
    const response = await fetch(url, {
        headers: {
            "Content-Type": "application/json",
            ...(options.headers || {})
        },
        ...options
    });
    const result = await response.json();
    if (result.code !== 200) {
        throw new Error(result.msg || "请求失败");
    }
    return result.data;
}

function showToast(message) {
    els.toast.textContent = message;
    els.toast.classList.add("show");
    window.clearTimeout(showToast.timer);
    showToast.timer = window.setTimeout(() => els.toast.classList.remove("show"), 2400);
}

function setUser(user) {
    state.user = user;
    if (user) {
        localStorage.setItem("financeUser", JSON.stringify(user));
        els.authPage.classList.add("hidden");
        els.appPage.classList.remove("hidden");
        els.loginUserText.textContent = `${user.nickname || user.username} · ID ${user.userId}`;
        loadAll().catch(error => showToast(error.message));
    } else {
        localStorage.removeItem("financeUser");
        els.appPage.classList.add("hidden");
        els.authPage.classList.remove("hidden");
    }
}

async function submitLogin(event) {
    event.preventDefault();
    const data = Object.fromEntries(new FormData(els.loginForm).entries());
    const user = await api("/api/auth/login", {
        method: "POST",
        body: JSON.stringify(data)
    });
    showToast("登录成功");
    setUser(user);
}

async function submitRegister(event) {
    event.preventDefault();
    const data = Object.fromEntries(new FormData(els.registerForm).entries());
    const user = await api("/api/auth/register", {
        method: "POST",
        body: JSON.stringify(data)
    });
    showToast("注册成功，已进入系统");
    setUser(user);
}

function fillDatalist(datalist, categories) {
    datalist.innerHTML = "";
    categories.forEach(category => {
        const option = document.createElement("option");
        option.value = category;
        datalist.appendChild(option);
    });
}

function syncCategoryOptions() {
    const type = Number(new FormData(els.transactionForm).get("type"));
    fillDatalist(els.transactionCategoryOptions, type === 1 ? incomeCategories : expenseCategories);
}

function knownCategories() {
    const dynamic = [
        ...state.transactions.map(item => item.category),
        ...state.budgets.map(item => item.category)
    ].filter(Boolean);
    return [...new Set([...allCategories, ...dynamic])];
}

function syncAllCategoryOptions() {
    const categories = knownCategories();
    fillDatalist(els.allCategoryOptions, categories);
    fillDatalist(els.budgetCategoryOptions, categories);
}

function initOptions() {
    fillDatalist(els.allCategoryOptions, allCategories);
    fillDatalist(els.budgetCategoryOptions, allCategories);
    syncCategoryOptions();
}

function switchView(target) {
    document.querySelectorAll(".view").forEach(view => {
        view.classList.toggle("active", view.id === target);
    });
    document.querySelectorAll(".nav-item").forEach(item => {
        item.classList.toggle("active", item.dataset.target === target);
    });
}

async function loadAll() {
    if (!state.user) {
        return;
    }

    state.month = els.monthInput.value || currentMonth();
    els.monthInput.value = state.month;
    els.sideMonth.textContent = state.month;
    els.budgetForm.elements.month.value = state.month;

    const userId = state.user.userId;
    const category = els.categoryFilter.value;
    const query = new URLSearchParams({ userId, month: state.month });
    if (category) {
        query.set("category", category);
    }

    const [report, transactions, budgets, overBudgets] = await Promise.all([
        api(`/api/transaction/report?userId=${userId}&month=${encodeURIComponent(state.month)}`),
        api(`/api/transaction/list?${query.toString()}`),
        api(`/api/budget/list?userId=${userId}&month=${encodeURIComponent(state.month)}`),
        api(`/api/budget/check?userId=${userId}&month=${encodeURIComponent(state.month)}`)
    ]);

    state.report = report;
    state.transactions = transactions || [];
    state.budgets = budgets || [];
    state.overBudgets = overBudgets || [];
    syncAllCategoryOptions();
    renderAll();
}

function renderAll() {
    renderMetrics();
    renderCategoryStats();
    renderTransactions();
    renderBudgets();
    renderOverBudgets();
    renderDailyTrend();
}

function renderMetrics() {
    const report = state.report || {};
    els.totalIncome.textContent = money(report.totalIncome);
    els.totalExpense.textContent = money(report.totalExpense);
    els.balance.textContent = money(report.balance);
    els.heroBalance.textContent = money(report.balance);
    els.overCount.textContent = state.overBudgets.length;
}

function renderCategoryStats() {
    const stats = state.report?.categoryStats || [];
    if (!stats.length) {
        els.categoryStats.innerHTML = `<div class="empty">本月暂无分类支出</div>`;
        return;
    }
    const max = Math.max(...stats.map(item => Number(item.total || 0)), 1);
    els.categoryStats.innerHTML = stats.map(item => {
        const percent = Math.max(6, Math.round(Number(item.total || 0) / max * 100));
        return `
            <div class="bar-row">
                <strong>${escapeHtml(item.category)}</strong>
                <div class="bar-track"><div class="bar-fill" style="width:${percent}%"></div></div>
                <span class="money">${money(item.total)}</span>
            </div>
        `;
    }).join("");
}

function renderTransactions() {
    els.recentCount.textContent = `${state.transactions.length} 条`;
    const recent = state.transactions.slice(0, 6);
    els.recentList.innerHTML = recent.length ? recent.map(item => `
        <div class="timeline-item">
            <div class="item-title">
                <strong>${escapeHtml(item.category)} <span class="tag ${item.type === 1 ? "income" : "expense"}">${item.type === 1 ? "收入" : "支出"}</span></strong>
                <small>${item.transactionDate}${item.description ? ` · ${escapeHtml(item.description)}` : ""}</small>
            </div>
            <strong class="money">${item.type === 1 ? "+" : "-"}${money(item.amount)}</strong>
        </div>
    `).join("") : `<div class="empty">本月暂无交易流水</div>`;

    els.transactionTable.innerHTML = state.transactions.length ? state.transactions.map(item => `
        <tr>
            <td>${item.transactionDate}</td>
            <td><span class="tag ${item.type === 1 ? "income" : "expense"}">${item.type === 1 ? "收入" : "支出"}</span></td>
            <td>${escapeHtml(item.category)}</td>
            <td class="money">${item.type === 1 ? "+" : "-"}${money(item.amount)}</td>
            <td>${escapeHtml(item.description || "")}</td>
            <td><button class="danger-btn" data-delete-transaction="${item.id}">删除</button></td>
        </tr>
    `).join("") : `<tr><td colspan="6"><div class="empty">本月暂无交易流水</div></td></tr>`;
}

function renderBudgets() {
    els.budgetCount.textContent = `${state.budgets.length} 条`;
    els.budgetList.innerHTML = state.budgets.length ? state.budgets.map(item => {
        const over = state.overBudgets.find(overItem => overItem.category === item.category);
        return `
            <div class="budget-item">
                <div class="item-title">
                    <strong>${escapeHtml(item.category)}${over ? ` <span class="tag expense">已超支</span>` : ""}</strong>
                    <small>${item.month} · 预算 ${money(item.monthlyLimit)}</small>
                </div>
                <div class="button-row">
                    <button class="mini-btn" data-edit-budget="${item.id}">编辑</button>
                    <button class="danger-btn" data-delete-budget="${item.id}">删除</button>
                </div>
            </div>
        `;
    }).join("") : `<div class="empty">本月还没有设置预算</div>`;
}

function renderOverBudgets() {
    els.overHint.textContent = state.overBudgets.length ? "需要控制支出" : "预算健康";
    els.overBudgetList.innerHTML = state.overBudgets.length ? state.overBudgets.map(item => `
        <div class="over-item">
            <div class="item-title">
                <strong>${escapeHtml(item.category)}</strong>
                <small>预算 ${money(item.monthlyLimit)} · 已花 ${money(item.actualExpense)}</small>
            </div>
            <strong class="money">超 ${money(item.overAmount)}</strong>
        </div>
    `).join("") : `<div class="empty">当前月份没有超支分类</div>`;
}

function renderDailyTrend() {
    const trend = state.report?.dailyTrend || {};
    const entries = Object.entries(trend);
    if (!entries.length) {
        els.dailyTrend.innerHTML = `<div class="empty">暂无每日支出趋势</div>`;
        els.sparkline.innerHTML = "";
        return;
    }
    const max = Math.max(...entries.map(([, value]) => Number(value || 0)), 1);
    els.dailyTrend.innerHTML = entries.map(([day, value]) => {
        const height = Math.max(8, Math.round(Number(value || 0) / max * 260));
        return `
            <div class="trend-item" title="${day} ${money(value)}">
                <span style="height:${height}px"></span>
                <small>${day.slice(8)}</small>
            </div>
        `;
    }).join("");

    els.sparkline.innerHTML = entries.slice(-12).map(([, value]) => {
        const height = Math.max(10, Math.round(Number(value || 0) / max * 90));
        return `<span style="height:${height}px"></span>`;
    }).join("");
}

async function submitTransaction(event) {
    event.preventDefault();
    const data = Object.fromEntries(new FormData(els.transactionForm).entries());
    data.userId = state.user.userId;
    data.type = Number(data.type);
    data.amount = Number(data.amount);
    await api("/api/transaction/add", {
        method: "POST",
        body: JSON.stringify(data)
    });
    els.transactionForm.reset();
    els.transactionForm.elements.transactionDate.value = today();
    syncCategoryOptions();
    showToast("交易已保存");
    await loadAll();
}

async function submitBudget(event) {
    event.preventDefault();
    const data = Object.fromEntries(new FormData(els.budgetForm).entries());
    data.userId = state.user.userId;
    data.monthlyLimit = Number(data.monthlyLimit);
    const isUpdate = Boolean(data.id);
    if (!isUpdate) {
        delete data.id;
    } else {
        data.id = Number(data.id);
    }
    await api(isUpdate ? "/api/budget/update" : "/api/budget/save", {
        method: isUpdate ? "PUT" : "POST",
        body: JSON.stringify(data)
    });
    resetBudgetForm();
    showToast(isUpdate ? "预算已更新" : "预算已保存");
    await loadAll();
}

async function handleDocumentClick(event) {
    const target = event.target;
    const authTab = target.dataset.authTab;
    if (authTab) {
        document.querySelectorAll(".auth-tab").forEach(tab => tab.classList.toggle("active", tab.dataset.authTab === authTab));
        els.loginForm.classList.toggle("active", authTab === "login");
        els.registerForm.classList.toggle("active", authTab === "register");
        return;
    }

    const viewTarget = target.dataset.target;
    if (viewTarget) {
        switchView(viewTarget);
        return;
    }

    const transactionId = target.dataset.deleteTransaction;
    if (transactionId && window.confirm("确认删除这条交易流水？")) {
        await api(`/api/transaction/${transactionId}`, { method: "DELETE" });
        showToast("交易已删除");
        await loadAll();
        return;
    }

    const budgetId = target.dataset.deleteBudget;
    if (budgetId && window.confirm("确认删除这条预算？")) {
        await api(`/api/budget/${budgetId}`, { method: "DELETE" });
        showToast("预算已删除");
        await loadAll();
        return;
    }

    const editBudgetId = target.dataset.editBudget;
    if (editBudgetId) {
        const budget = state.budgets.find(item => String(item.id) === String(editBudgetId));
        if (budget) {
            els.budgetFormTitle.textContent = "编辑预算";
            els.budgetForm.elements.id.value = budget.id;
            els.budgetForm.elements.category.value = budget.category;
            els.budgetForm.elements.month.value = budget.month;
            els.budgetForm.elements.monthlyLimit.value = budget.monthlyLimit;
            switchView("budgets");
        }
    }
}

function resetBudgetForm() {
    els.budgetForm.reset();
    els.budgetFormTitle.textContent = "设置预算";
    els.budgetForm.elements.id.value = "";
    els.budgetForm.elements.month.value = state.month;
}

function escapeHtml(value) {
    return String(value)
        .replaceAll("&", "&amp;")
        .replaceAll("<", "&lt;")
        .replaceAll(">", "&gt;")
        .replaceAll('"', "&quot;")
        .replaceAll("'", "&#039;");
}

function bindEvents() {
    document.addEventListener("click", event => handleDocumentClick(event).catch(error => showToast(error.message)));
    els.loginForm.addEventListener("submit", event => submitLogin(event).catch(error => showToast(error.message)));
    els.registerForm.addEventListener("submit", event => submitRegister(event).catch(error => showToast(error.message)));
    els.logoutBtn.addEventListener("click", () => setUser(null));
    els.refreshBtn.addEventListener("click", () => loadAll().then(() => showToast("数据已刷新")).catch(error => showToast(error.message)));
    els.monthInput.addEventListener("change", () => loadAll().catch(error => showToast(error.message)));
    els.categoryFilter.addEventListener("change", () => loadAll().catch(error => showToast(error.message)));
    els.transactionForm.addEventListener("submit", event => submitTransaction(event).catch(error => showToast(error.message)));
    els.budgetForm.addEventListener("submit", event => submitBudget(event).catch(error => showToast(error.message)));
    els.resetBudgetBtn.addEventListener("click", resetBudgetForm);
    els.transactionForm.querySelectorAll("input[name='type']").forEach(input => {
        input.addEventListener("change", syncCategoryOptions);
    });
}

function bootstrap() {
    const month = currentMonth();
    els.monthInput.value = month;
    els.transactionForm.elements.transactionDate.value = today();
    initOptions();
    bindEvents();

    const savedUser = localStorage.getItem("financeUser");
    if (savedUser) {
        setUser(JSON.parse(savedUser));
    }
}

bootstrap();
