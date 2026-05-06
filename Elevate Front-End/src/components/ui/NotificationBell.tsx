import { useState, useEffect, useRef } from 'react';
import { Bell, Check, CheckCircle2 } from 'lucide-react';
import { notificationService } from '../../services/notificationService';
import type { NotificationResponse } from '../../types';

export default function NotificationBell() {
  const [notifications, setNotifications] = useState<NotificationResponse[]>([]);
  const [isOpen, setIsOpen] = useState(false);
  const dropdownRef = useRef<HTMLDivElement>(null);

  const fetchNotifications = async () => {
    try {
      const data = await notificationService.getUnreadNotifications();
      // Assuming GET /api/notifications returns all recent notifications.
      // We will sort them by created date safely.
      const sorted = data.sort((a, b) => new Date(b.createdAt).getTime() - new Date(a.createdAt).getTime());
      setNotifications(sorted);
    } catch (err) {
      console.error('Failed to fetch notifications', err);
    }
  };

  useEffect(() => {
    fetchNotifications();
    // Poll every 60 seconds
    const interval = setInterval(fetchNotifications, 60000);
    return () => clearInterval(interval);
  }, []);

  useEffect(() => {
    function handleClickOutside(e: MouseEvent) {
      if (dropdownRef.current && !dropdownRef.current.contains(e.target as Node)) {
        setIsOpen(false);
      }
    }
    document.addEventListener('mousedown', handleClickOutside);
    return () => document.removeEventListener('mousedown', handleClickOutside);
  }, []);

  const unreadCount = notifications.filter(n => !n.isRead).length;

  const handleMarkAsRead = async (e: React.MouseEvent, id: number) => {
    e.stopPropagation();
    try {
      await notificationService.markAsRead(id);
      setNotifications(prev => 
        prev.map(n => n.id === id ? { ...n, isRead: true } : n)
      );
    } catch (err) {
      console.error('Failed to mark as read', err);
    }
  };

  const handleMarkAllAsRead = async (e: React.MouseEvent) => {
    e.stopPropagation();
    try {
      await notificationService.markAllAsRead();
      setNotifications(prev => prev.map(n => ({ ...n, isRead: true })));
    } catch (err) {
      console.error('Failed to mark all as read', err);
      // Fallback: individually mark unread ones if the bulk endpoint doesn't exist
      notifications.filter(n => !n.isRead).forEach(n => {
         notificationService.markAsRead(n.id).catch(() => {});
      });
      setNotifications(prev => prev.map(n => ({ ...n, isRead: true })));
    }
  };

  return (
    <div className="relative" ref={dropdownRef}>
      <button
        onClick={() => setIsOpen(!isOpen)}
        className="relative p-2 rounded-xl text-surface-400 hover:text-surface-100 hover:bg-surface-800/60 transition-all focus:outline-none"
        aria-label="Notifications"
      >
        <Bell className="w-5 h-5" />
        {unreadCount > 0 && (
          <span className="absolute top-1.5 right-1.5 flex h-2.5 w-2.5">
            <span className="animate-ping absolute inline-flex h-full w-full rounded-full bg-red-400 opacity-75"></span>
            <span className="relative inline-flex rounded-full h-2.5 w-2.5 bg-red-500 border-2 border-surface-950"></span>
          </span>
        )}
      </button>

      {isOpen && (
        <div className="absolute right-0 mt-2 w-80 sm:w-96 rounded-xl glass-strong shadow-2xl shadow-black/40 py-2 animate-slide-down z-50 overflow-hidden border border-surface-700/50">
          <div className="px-4 py-3 border-b border-surface-700/40 flex items-center justify-between bg-surface-900/50">
            <h3 className="text-sm font-bold text-surface-100">Notifications</h3>
            {unreadCount > 0 && (
              <button 
                onClick={handleMarkAllAsRead}
                className="text-xs font-semibold text-primary-400 hover:text-primary-300 flex items-center gap-1 transition-colors"
              >
                <CheckCircle2 className="w-3.5 h-3.5" /> Mark all read
              </button>
            )}
          </div>
          
          <div className="max-h-[60vh] overflow-y-auto overscroll-contain">
            {notifications.length > 0 ? (
              <div className="divide-y divide-surface-800/50">
                {notifications.map((notification) => (
                  <div 
                    key={notification.id} 
                    className={`flex items-start gap-3 p-4 transition-colors ${!notification.isRead ? 'bg-primary-500/5 hover:bg-primary-500/10' : 'hover:bg-surface-800/40'}`}
                  >
                    {!notification.isRead && (
                      <div className="w-2 h-2 rounded-full bg-primary-500 shrink-0 mt-1.5"></div>
                    )}
                    <div className="flex-1 min-w-0">
                      <p className={`text-sm ${!notification.isRead ? 'text-surface-100 font-semibold' : 'text-surface-300'}`}>
                        {notification.message}
                      </p>
                      <p className="text-xs text-surface-500 mt-1">
                        {new Date(notification.createdAt).toLocaleString()}
                      </p>
                    </div>
                    {!notification.isRead && (
                      <button 
                        onClick={(e) => handleMarkAsRead(e, notification.id)}
                        className="p-1.5 rounded-lg text-surface-400 hover:text-primary-400 hover:bg-primary-500/10 transition-colors shrink-0"
                        title="Mark as read"
                      >
                         <Check className="w-4 h-4" />
                      </button>
                    )}
                  </div>
                ))}
              </div>
            ) : (
              <div className="px-4 py-8 text-center text-surface-400 text-sm flex flex-col items-center">
                <Bell className="w-8 h-8 mb-2 opacity-20" />
                No new notifications
              </div>
            )}
          </div>
        </div>
      )}
    </div>
  );
}
