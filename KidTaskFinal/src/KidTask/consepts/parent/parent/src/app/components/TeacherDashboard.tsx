import React, { useState } from 'react';
import { Plus, Calendar, Star, LogOut, Settings, User, BookOpen, FileText } from 'lucide-react';

interface StudentSubmission {
  id: number;
  studentName: string;
  taskTitle: string;
  submittedDate: string;
  rating: number;
}

type ViewMode = 'add-task' | 'schedule' | 'rate-tasks';

export function TeacherDashboard() {
  const [showProfileMenu, setShowProfileMenu] = useState(false);
  const [activeView, setActiveView] = useState<ViewMode>('add-task');
  const [formData, setFormData] = useState({
    title: '',
    description: '',
    dueDate: '',
    points: ''
  });

  const [submissions, setSubmissions] = useState<StudentSubmission[]>([
    { id: 1, studentName: 'Emily Johnson', taskTitle: 'Math Homework Chapter 5', submittedDate: '2024-12-27', rating: 0 },
    { id: 2, studentName: 'Michael Chen', taskTitle: 'Science Project Report', submittedDate: '2024-12-27', rating: 0 },
    { id: 3, studentName: 'Sarah Williams', taskTitle: 'Reading Assignment', submittedDate: '2024-12-26', rating: 0 },
    { id: 4, studentName: 'David Martinez', taskTitle: 'History Essay', submittedDate: '2024-12-26', rating: 0 },
  ]);

  const classInfo = {
    className: '4-B',
    totalStudents: 24,
    completionAverage: 75
  };

  const handleRateSubmission = (submissionId: number, rating: number) => {
    setSubmissions(submissions.map(sub => 
      sub.id === submissionId ? { ...sub, rating } : sub
    ));
  };

  const handleFormChange = (e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>) => {
    setFormData({ ...formData, [e.target.name]: e.target.value });
  };

  const handleSubmitTask = (e: React.FormEvent) => {
    e.preventDefault();
    console.log('Task submitted:', formData);
    // Reset form
    setFormData({ title: '', description: '', dueDate: '', points: '' });
  };

  return (
    <div className="min-h-screen relative overflow-hidden">
      {/* Background with lavender gradients */}
      <div className="absolute inset-0 bg-gradient-to-br from-[#FAF9FC] via-white to-[#F5F3F7]"></div>
      
      {/* Corner gradients - Lavender/Purple */}
      <div className="absolute top-0 left-0 w-96 h-96 bg-[#E6E6FA] rounded-full opacity-30 blur-3xl"></div>
      <div className="absolute bottom-0 right-0 w-96 h-96 bg-[#E6E6FA] rounded-full opacity-30 blur-3xl"></div>

      {/* Fixed Left Sidebar */}
      <aside className="fixed left-0 top-0 h-screen w-64 bg-white/20 backdrop-blur-[15px] border-r border-white/30 shadow-lg z-40">
        <div className="p-6">
          {/* KidTask Logo */}
          <div className="flex items-center space-x-2 mb-12">
            <svg width="32" height="32" viewBox="0 0 48 48" fill="none">
              <rect x="10" y="8" width="28" height="36" rx="2" fill="#0056B3" stroke="#0056B3" strokeWidth="2"/>
              <circle cx="16" cy="8" r="2" fill="#0056B3"/>
              <circle cx="24" cy="8" r="2" fill="#0056B3"/>
              <circle cx="32" cy="8" r="2" fill="#0056B3"/>
              <line x1="15" y1="18" x2="33" y2="18" stroke="white" strokeWidth="1.5"/>
              <line x1="15" y1="24" x2="33" y2="24" stroke="white" strokeWidth="1.5"/>
              <line x1="15" y1="30" x2="28" y2="30" stroke="white" strokeWidth="1.5"/>
            </svg>
            <span className="font-bold text-xl text-[#0056B3]">KidTask</span>
          </div>

          {/* Navigation Items */}
          <nav className="space-y-2">
            <button 
              onClick={() => setActiveView('add-task')}
              className={`w-full flex items-center space-x-3 px-4 py-3 rounded-xl transition-all duration-200 ${
                activeView === 'add-task' 
                  ? 'bg-[#9966CC]/20' 
                  : 'hover:bg-[#9966CC]/10'
              }`}
            >
              <Plus className="w-5 h-5 text-[#0056B3]" style={{ color: '#9966CC' }} />
              <span className="text-[#0056B3] font-medium">Add School Task</span>
            </button>
            <button 
              onClick={() => setActiveView('schedule')}
              className={`w-full flex items-center space-x-3 px-4 py-3 rounded-xl transition-all duration-200 ${
                activeView === 'schedule' 
                  ? 'bg-[#9966CC]/20' 
                  : 'hover:bg-[#9966CC]/10'
              }`}
            >
              <Calendar className="w-5 h-5" style={{ color: '#9966CC' }} />
              <span className="text-[#0056B3] font-medium">View Schedule</span>
            </button>
            <button 
              onClick={() => setActiveView('rate-tasks')}
              className={`w-full flex items-center space-x-3 px-4 py-3 rounded-xl transition-all duration-200 ${
                activeView === 'rate-tasks' 
                  ? 'bg-[#9966CC]/20' 
                  : 'hover:bg-[#9966CC]/10'
              }`}
            >
              <Star className="w-5 h-5" style={{ color: '#9966CC' }} />
              <span className="text-[#0056B3] font-medium">Rate Tasks</span>
            </button>
          </nav>
        </div>
      </aside>

      {/* Main Content Area */}
      <div className="ml-64 relative z-10">
        {/* Top Header Bar */}
        <header className="sticky top-0 z-30 p-6">
          <div className="flex items-center justify-between">
            {/* Center Progress Card */}
            <div className="flex-1 max-w-2xl mx-auto">
              <div className="bg-white/30 backdrop-blur-xl border border-white/40 rounded-2xl shadow-xl p-6">
                {/* Top Row: Class Info */}
                <div className="flex items-center justify-between mb-4">
                  <div className="flex items-center space-x-2">
                    <BookOpen className="w-5 h-5 text-[#0056B3]" />
                    <span className="font-bold text-[#0056B3]">Class:</span>
                    <span className="font-bold text-[#0056B3] text-xl">{classInfo.className}</span>
                  </div>
                  <div className="flex items-center space-x-2">
                    <User className="w-5 h-5 text-[#9966CC]" />
                    <span className="font-bold text-[#0056B3]">Total Students:</span>
                    <span className="font-bold text-[#0056B3] text-xl">{classInfo.totalStudents}</span>
                  </div>
                </div>

                {/* Bottom Row: Class Completion Average */}
                <div className="space-y-2">
                  <span className="font-semibold text-[#0056B3] text-sm">Class Completion Average</span>
                  <div className="flex items-center space-x-3">
                    <div className="flex-1 h-[10px] bg-gray-200 rounded-full overflow-hidden">
                      <div 
                        className="h-full bg-[#9966CC] transition-all duration-500"
                        style={{ width: `${classInfo.completionAverage}%` }}
                      ></div>
                    </div>
                    <span className="font-bold text-[#0056B3] text-sm">{classInfo.completionAverage}%</span>
                  </div>
                </div>
              </div>
            </div>

            {/* Profile Avatar & Dropdown */}
            <div className="relative ml-6">
              <button 
                onClick={() => setShowProfileMenu(!showProfileMenu)}
                className="w-12 h-12 rounded-full bg-gradient-to-br from-[#9966CC] to-[#B88FD9] flex items-center justify-center hover:scale-105 transition-transform duration-200 shadow-lg"
              >
                <User className="w-6 h-6 text-white" />
              </button>

              {/* Dropdown Menu */}
              {showProfileMenu && (
                <div className="absolute right-0 mt-2 w-56 bg-white/90 backdrop-blur-xl border border-white/40 rounded-xl shadow-2xl overflow-hidden">
                  <button className="w-full flex items-center space-x-3 px-4 py-3 hover:bg-[#9966CC]/10 transition-colors">
                    <Settings className="w-5 h-5 text-[#0056B3]" />
                    <span className="text-[#0056B3] font-medium">Profile Settings</span>
                  </button>
                  <button className="w-full flex items-center space-x-3 px-4 py-3 hover:bg-[#9966CC]/10 transition-colors border-t border-gray-200">
                    <LogOut className="w-5 h-5 text-[#0056B3]" />
                    <span className="text-[#0056B3] font-medium">Logout</span>
                  </button>
                </div>
              )}
            </div>
          </div>
        </header>

        {/* Main Dynamic Content Area */}
        <main className="p-6">
          <div className="max-w-4xl mx-auto">
            {/* Add Task View */}
            {activeView === 'add-task' && (
              <div>
                <h1 className="text-3xl font-bold text-[#0056B3] mb-6">Add School Task</h1>
                
                <div className="bg-white/40 backdrop-blur-lg border border-white/50 rounded-2xl shadow-lg p-8">
                  <form onSubmit={handleSubmitTask} className="space-y-6">
                    <div>
                      <label className="block text-[#0056B3] font-semibold mb-2">Task Title</label>
                      <input
                        type="text"
                        name="title"
                        value={formData.title}
                        onChange={handleFormChange}
                        className="w-full px-4 py-3 bg-white/50 border border-white/60 rounded-xl focus:outline-none focus:ring-2 focus:ring-[#9966CC] transition-all"
                        placeholder="Enter task title"
                        required
                      />
                    </div>

                    <div>
                      <label className="block text-[#0056B3] font-semibold mb-2">Description</label>
                      <textarea
                        name="description"
                        value={formData.description}
                        onChange={handleFormChange}
                        rows={4}
                        className="w-full px-4 py-3 bg-white/50 border border-white/60 rounded-xl focus:outline-none focus:ring-2 focus:ring-[#9966CC] transition-all resize-none"
                        placeholder="Enter task description"
                        required
                      />
                    </div>

                    <div className="grid grid-cols-2 gap-4">
                      <div>
                        <label className="block text-[#0056B3] font-semibold mb-2">Due Date</label>
                        <input
                          type="date"
                          name="dueDate"
                          value={formData.dueDate}
                          onChange={handleFormChange}
                          className="w-full px-4 py-3 bg-white/50 border border-white/60 rounded-xl focus:outline-none focus:ring-2 focus:ring-[#9966CC] transition-all"
                          required
                        />
                      </div>

                      <div>
                        <label className="block text-[#0056B3] font-semibold mb-2">Points</label>
                        <input
                          type="number"
                          name="points"
                          value={formData.points}
                          onChange={handleFormChange}
                          className="w-full px-4 py-3 bg-white/50 border border-white/60 rounded-xl focus:outline-none focus:ring-2 focus:ring-[#9966CC] transition-all"
                          placeholder="0"
                          min="0"
                          required
                        />
                      </div>
                    </div>

                    <button
                      type="submit"
                      className="w-full px-6 py-4 bg-gradient-to-r from-[#9966CC] to-[#B88FD9] text-white rounded-xl font-semibold hover:shadow-lg hover:scale-[1.02] transition-all duration-200"
                    >
                      Create Task
                    </button>
                  </form>
                </div>
              </div>
            )}

            {/* View Schedule View */}
            {activeView === 'schedule' && (
              <div>
                <h1 className="text-3xl font-bold text-[#0056B3] mb-6">Class Schedule</h1>
                
                <div className="bg-white/40 backdrop-blur-lg border border-white/50 rounded-2xl shadow-lg p-8">
                  <div className="text-center py-12">
                    <Calendar className="w-16 h-16 mx-auto mb-4" style={{ color: '#9966CC' }} />
                    <p className="text-[#0056B3] text-lg">Schedule view coming soon...</p>
                  </div>
                </div>
              </div>
            )}

            {/* Rate Tasks View */}
            {activeView === 'rate-tasks' && (
              <div>
                <h1 className="text-3xl font-bold text-[#0056B3] mb-6">Rate Student Submissions</h1>
                
                <div className="space-y-4">
                  {submissions.map((submission) => (
                    <div 
                      key={submission.id}
                      className="bg-white/40 backdrop-blur-lg border border-white/50 rounded-2xl shadow-lg p-6 hover:shadow-xl transition-all duration-300"
                    >
                      <div className="flex items-start justify-between mb-4">
                        <div className="flex-1">
                          <h3 className="font-bold text-lg text-gray-800 mb-1">
                            {submission.studentName}
                          </h3>
                          <p className="text-[#0056B3] font-medium mb-1">
                            {submission.taskTitle}
                          </p>
                          <p className="text-gray-600 text-sm">
                            Submitted: {new Date(submission.submittedDate).toLocaleDateString()}
                          </p>
                        </div>
                        
                        <FileText className="w-6 h-6 text-[#9966CC] flex-shrink-0" />
                      </div>

                      {/* Star Rating */}
                      <div className="flex items-center space-x-2 pt-4 border-t border-white/50">
                        <span className="text-[#0056B3] font-semibold mr-2">Rate:</span>
                        {[1, 2, 3, 4, 5].map((star) => (
                          <button
                            key={star}
                            onClick={() => handleRateSubmission(submission.id, star)}
                            className="transition-all duration-200 hover:scale-110"
                          >
                            <Star
                              className={`w-7 h-7 ${
                                star <= submission.rating
                                  ? 'fill-[#FFD700] text-[#FFD700]'
                                  : 'text-gray-300'
                              }`}
                            />
                          </button>
                        ))}
                        {submission.rating > 0 && (
                          <span className="ml-2 text-[#9966CC] font-semibold">
                            {submission.rating}/5
                          </span>
                        )}
                      </div>
                    </div>
                  ))}
                </div>
              </div>
            )}
          </div>
        </main>
      </div>
    </div>
  );
}
