// Theme Management
const storageKey = 'goal-sync-theme';

function setTheme(theme) {
    document.documentElement.setAttribute('data-theme', theme);
    localStorage.setItem(storageKey, theme);
    updateToggleUI(theme);
}

function updateToggleUI(theme) {
    const thumb = document.querySelector('.toggle-thumb');
    if (thumb) {
        // Logic handled by CSS [data-theme="light"]
    }
}

function toggleTheme() {
    const current = localStorage.getItem(storageKey) || 'dark';
    const next = current === 'dark' ? 'light' : 'dark';
    setTheme(next);
}

// Initialize Theme
const savedTheme = localStorage.getItem(storageKey) || 'dark';
setTheme(savedTheme);

// Notifications logic
function toggleNotifications() {
    const dropdown = document.getElementById('notification-dropdown');
    if (dropdown) dropdown.classList.toggle('active');
}

function fetchNotifications() {
    fetch('GetNotificationsServlet')
        .then(res => res.json())
        .then(data => {
            const list = document.getElementById('notification-list');
            const badge = document.getElementById('notification-badge');
            if (!list) return;

            if (data.notifications && data.notifications.length > 0) {
                const unreadCount = data.notifications.filter(n => !n.isRead).length;
                badge.innerText = unreadCount;
                badge.style.display = unreadCount > 0 ? 'block' : 'none';

                list.innerHTML = data.notifications.map(n => `
                    <div class="notification-item ${n.isRead ? '' : 'unread'}" onclick="markAsRead(${n.id})">
                        <div class="notification-msg">${n.message}</div>
                        <div class="notification-time">${n.timeAgo}</div>
                    </div>
                `).join('');
            } else {
                list.innerHTML = '<div style="padding:20px; text-align:center; color:var(--text-muted);">No notifications</div>';
                badge.style.display = 'none';
            }
        })
        .catch(err => console.error('Error fetching notifications:', err));
}

function markAsRead(id) {
    fetch(`MarkNotificationReadServlet?id=${id}`)
        .then(() => fetchNotifications());
}

// Click outside to close notifications
window.onclick = function(event) {
    if (!event.target.closest('.notification-bell')) {
        const dropdown = document.getElementById('notification-dropdown');
        if (dropdown && dropdown.classList.contains('active')) {
            dropdown.classList.remove('active');
        }
    }
}

// Initial fetch
if (document.getElementById('notification-list')) {
    fetchNotifications();
    setInterval(fetchNotifications, 30000); // Refresh every 30s
}
