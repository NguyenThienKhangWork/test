import React, { useState, useEffect } from 'react';
import toast from 'react-hot-toast';
import API from '../api/axios';

export default function AdminDashboard() {
  const [activeTab, setActiveTab] = useState('analytics');
  
  // Withdrawals State
  const [withdrawals, setWithdrawals] = useState([]);
  const [loadingWithdrawals, setLoadingWithdrawals] = useState(false);

  // Analytics State
  const [stats, setStats] = useState({
    revenue: '0 VND',
    transactions: '0',
    newUsers: '0',
    topExperts: []
  });
  const [loadingStats, setLoadingStats] = useState(false);

  // Users State
  const [users, setUsers] = useState([]);
  const [loadingUsers, setLoadingUsers] = useState(false);

  // Disputes State
  const [disputes, setDisputes] = useState([]);
  const [loadingDisputes, setLoadingDisputes] = useState(false);

  // Content State
  const [jobs, setJobs] = useState([]);
  const [services, setServices] = useState([]);
  const [reviews, setReviews] = useState([]);
  const [loadingContent, setLoadingContent] = useState(false);

  useEffect(() => {
    if (activeTab === 'analytics') {
      fetchAnalytics();
    } else if (activeTab === 'users') {
      fetchUsers();
    } else if (activeTab === 'disputes') {
      fetchDisputes();
    } else if (activeTab === 'withdrawals') {
      fetchWithdrawals();
    } else if (activeTab === 'content') {
      fetchContent();
    }
  }, [activeTab]);

  const fetchAnalytics = async () => {
    setLoadingStats(true);
    try {
      const response = await API.get('/admin/analytics');
      const { totalRevenue, totalTransactions, newUsersCount, topExperts } = response.data;
      setStats({
        revenue: totalRevenue?.toLocaleString() + ' VND',
        transactions: totalTransactions + ' giao dịch',
        newUsers: newUsersCount + ' người dùng mới',
        topExperts: topExperts || []
      });
    } catch (err) {
      toast.error('Lỗi khi tải thống kê từ hệ thống!');
    } finally {
      setLoadingStats(false);
    }
  };

  const fetchUsers = async () => {
    setLoadingUsers(true);
    try {
      const response = await API.get('/admin/users');
      setUsers(response.data || []);
    } catch (err) {
      toast.error('Lỗi khi tải danh sách thành viên!');
    } finally {
      setLoadingUsers(false);
    }
  };

  const toggleUserStatus = async (userId) => {
    try {
      const response = await API.put(`/admin/users/${userId}/toggle-lock`);
      toast.success(`ĐÃ CẬP NHẬT TRẠNG THÁI TÀI KHOẢN THÀNH CÔNG!`);
      setUsers(prev => prev.map(u => u.id === userId ? response.data : u));
    } catch (err) {
      toast.error('Lỗi khi cập nhật trạng thái tài khoản.');
    }
  };

  const fetchDisputes = async () => {
    setLoadingDisputes(true);
    try {
      const response = await API.get('/admin/disputes');
      setDisputes(response.data || []);
    } catch (err) {
      toast.error('Lỗi khi tải danh sách tranh chấp!');
    } finally {
      setLoadingDisputes(false);
    }
  };

  const handleResolveDispute = async (disputeId, action) => {
    try {
      const response = await API.put(`/admin/disputes/${disputeId}/resolve?resolution=${action}`);
      toast.success(`ĐÃ XỬ LÝ KHIẾU NẠI THÀNH CÔNG! HỢP ĐỒNG ĐÃ ĐƯỢC GIẢI QUYẾT.`);
      setDisputes(prev => prev.map(d => d.id === disputeId ? response.data : d));
    } catch (err) {
      toast.error('Lỗi khi xử lý khiếu nại tranh chấp.');
    }
  };

  const fetchWithdrawals = async () => {
    setLoadingWithdrawals(true);
    try {
      const response = await API.get('/withdrawals');
      setWithdrawals(response.data || []);
    } catch (err) {
      toast.error('Lỗi khi tải danh sách yêu cầu rút tiền!');
    } finally {
      setLoadingWithdrawals(false);
    }
  };

  const handleApproveWithdrawal = async (wId) => {
    try {
      const response = await API.put(`/withdrawals/${wId}/approve`);
      toast.success('ĐÃ PHÊ DUYỆT RÚT TIỀN THÀNH CÔNG! TIỀN ĐÃ ĐƯỢC TRỪ VÀO SỐ DƯ CỦA EXPERT.');
      setWithdrawals(prev => prev.map(w => w.id === wId ? response.data : w));
    } catch (err) {
      toast.error(err.response?.data?.message || 'Lỗi khi duyệt rút tiền.');
    }
  };

  const handleRejectWithdrawal = async (wId) => {
    try {
      const response = await API.put(`/withdrawals/${wId}/reject`);
      toast.success('ĐÃ TỪ CHỐI YÊU CẦU RÚT TIỀN.');
      setWithdrawals(prev => prev.map(w => w.id === wId ? response.data : w));
    } catch (err) {
      toast.error(err.response?.data?.message || 'Lỗi khi từ chối rút tiền.');
    }
  };

  const fetchContent = async () => {
    setLoadingContent(true);
    try {
      const [jobsRes, servicesRes, reviewsRes] = await Promise.all([
        API.get('/jobs').catch(() => ({ data: [] })),
        API.get('/services').catch(() => ({ data: [] })),
        API.get('/admin/reviews').catch(() => ({ data: [] }))
      ]);
      setJobs(jobsRes.data || []);
      setServices(servicesRes.data || []);
      setReviews(reviewsRes.data || []);
    } catch (err) {
      toast.error('Lỗi khi tải nội dung bài đăng!');
    } finally {
      setLoadingContent(false);
    }
  };

  const handleDeleteJob = async (jobId) => {
    if (!window.confirm('Bạn có chắc chắn muốn xóa vĩnh viễn bài đăng Job này?')) return;
    try {
      await API.delete(`/admin/jobs/${jobId}`);
      toast.success('ĐÃ XÓA TIN ĐĂNG TUYỂN DỤNG KHỎI HỆ THỐNG.');
      setJobs(prev => prev.filter(j => j.id !== jobId));
    } catch (err) {
      toast.error('Lỗi khi xóa bài đăng.');
    }
  };

  const handleDeleteService = async (serviceId) => {
    if (!window.confirm('Bạn có chắc chắn muốn xóa vĩnh viễn gói dịch vụ này khỏi Marketplace?')) return;
    try {
      await API.delete(`/admin/services/${serviceId}`);
      toast.success('ĐÃ GỠ GÓI DỊCH VỤ KHỎI MARKETPLACE.');
      setServices(prev => prev.filter(s => s.id !== serviceId));
    } catch (err) {
      toast.error('Lỗi khi gỡ dịch vụ.');
    }
  };

  const handleDeleteReview = async (reviewId) => {
    if (!window.confirm('Bạn có chắc chắn muốn xóa vĩnh viễn đánh giá này?')) return;
    try {
      await API.delete(`/admin/reviews/${reviewId}`);
      toast.success('ĐÃ XÓA ĐÁNH GIÁ KHỎI HỆ THỐNG.');
      setReviews(prev => prev.filter(r => r.id !== reviewId));
    } catch (err) {
      toast.error('Lỗi khi xóa đánh giá.');
    }
  };

  return (
    <div style={{ minHeight: '100vh', padding: '120px 20px 60px', position: 'relative' }}>
      <div className="grid-bg"></div>
      <div className="scanlines"></div>

      <div className="container" style={{ position: 'relative', zIndex: 2 }}>
        
        {/* Header */}
        <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '35px', borderBottom: '1px solid rgba(0,240,255,0.1)', paddingBottom: '20px', flexWrap: 'wrap', gap: '15px' }}>
          <div>
            <h1 className="gradient-text" style={{ fontSize: '2.5rem', margin: 0, fontFamily: 'var(--font-orbitron)' }}>
              ADMIN_CONTROL_CENTER
            </h1>
            <p style={{ fontFamily: 'var(--font-mono)', color: 'var(--cp-text-muted)', marginTop: '5px' }}>
              // HỆ THỐNG ĐIỀU HÀNH TỐI CAO AI TASKER | QUẢN TRỊ VIÊN
            </p>
          </div>

          <div style={{ display: 'flex', gap: '10px', flexWrap: 'wrap' }}>
            <button 
              onClick={() => setActiveTab('analytics')} 
              className={`btn ${activeTab === 'analytics' ? 'btn-primary' : 'btn-outline'}`}
            >
              <span>📊 STATS</span>
            </button>
            <button 
              onClick={() => setActiveTab('users')} 
              className={`btn ${activeTab === 'users' ? 'btn-primary' : 'btn-outline'}`}
            >
              <span>👥 USERS</span>
            </button>
            <button 
              onClick={() => setActiveTab('content')} 
              className={`btn ${activeTab === 'content' ? 'btn-primary' : 'btn-outline'}`}
            >
              <span>📂 CONTENT</span>
            </button>
            <button 
              onClick={() => setActiveTab('disputes')} 
              className={`btn ${activeTab === 'disputes' ? 'btn-primary' : 'btn-outline'}`}
            >
              <span>⚖️ DISPUTES ({disputes.filter(d => d.status === 'PENDING').length})</span>
            </button>
            <button 
              onClick={() => setActiveTab('withdrawals')} 
              className={`btn ${activeTab === 'withdrawals' ? 'btn-primary' : 'btn-outline'}`}
            >
              <span>💸 WITHDRAWALS ({withdrawals.filter(w => w.status === 'PENDING').length})</span>
            </button>
          </div>
        </div>

        {/* Tab 1: Analytics Dashboard */}
        {activeTab === 'analytics' && (
          <div>
            {loadingStats ? (
              <div style={{ color: 'var(--cp-cyan)', fontFamily: 'var(--font-mono)', padding: '20px' }}>
                ⚡ ĐANG KẾT NỐI VÀ TRÍCH XUẤT SỐ LIỆU ANALYTICS...
              </div>
            ) : (
              <>
                <div className="stats-grid" style={{ marginBottom: '40px', gridTemplateColumns: 'repeat(3, 1fr)' }}>
                  <div className="stat-card">
                    <div className="stat-value" style={{ fontSize: '2.2rem', color: 'var(--cp-cyan)' }}>{stats.revenue}</div>
                    <div className="stat-label">// TỔNG DOANH THU NỀN TẢNG</div>
                  </div>
                  <div className="stat-card">
                    <div className="stat-value" style={{ fontSize: '2.2rem', color: 'var(--cp-magenta)' }}>{stats.transactions}</div>
                    <div className="stat-label">// SỐ LƯỢNG GIAO DỊCH KÝ QUỸ</div>
                  </div>
                  <div className="stat-card">
                    <div className="stat-value" style={{ fontSize: '2.2rem', color: '#39ff14' }}>{stats.newUsers}</div>
                    <div className="stat-label">// THÀNH VIÊN MỚI TRONG THÁNG</div>
                  </div>
                </div>

                <div className="cta-box" style={{ padding: '30px', border: '1px solid rgba(0,240,255,0.15)', background: 'rgba(5,5,10,0.85)' }}>
                  <h2 style={{ fontFamily: 'var(--font-orbitron)', fontSize: '1.3rem', color: 'var(--cp-cyan)', borderBottom: '1px dashed rgba(0,240,255,0.2)', paddingBottom: '10px', marginBottom: '20px' }}>
                    TOP CHUYÊN GIA AI HIỆU SUẤT CAO
                  </h2>
                  <div style={{ display: 'flex', flexDirection: 'column', gap: '15px' }}>
                    {stats.topExperts.map((exp, idx) => (
                      <div key={idx} style={{ display: 'flex', justifyContent: 'space-between', padding: '12px 18px', background: 'rgba(255,255,255,0.02)', border: '1px solid rgba(255,255,255,0.05)', fontFamily: 'var(--font-mono)', alignItems: 'center' }}>
                        <span style={{ color: '#fff', fontWeight: 'bold' }}>🏆 TOP {idx + 1}: {exp.name}</span>
                        <span style={{ color: 'var(--cp-cyan)' }}>Điểm đánh giá: {exp.rating} ⭐</span>
                        <span style={{ color: 'var(--cp-magenta)' }}>Số dư tích lũy: {exp.income?.toLocaleString()} VND</span>
                      </div>
                    ))}
                  </div>
                </div>
              </>
            )}
          </div>
        )}

        {/* Tab 2: User management */}
        {activeTab === 'users' && (
          <div className="cta-box" style={{ padding: '30px', border: '1px solid rgba(0,240,255,0.15)', background: 'rgba(5,5,10,0.85)' }}>
            <h2 style={{ fontFamily: 'var(--font-orbitron)', fontSize: '1.3rem', color: 'var(--cp-cyan)', borderBottom: '1px dashed rgba(0,240,255,0.2)', paddingBottom: '10px', marginBottom: '20px' }}>
              DANH SÁCH QUẢN LÝ THÀNH VIÊN
            </h2>
            {loadingUsers ? (
              <div style={{ color: 'var(--cp-cyan)', fontFamily: 'var(--font-mono)' }}>⚡ ĐANG TẢI...</div>
            ) : (
              <div style={{ display: 'flex', flexDirection: 'column', gap: '15px' }}>
                {users.map(u => (
                  <div key={u.id} style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', padding: '15px', background: 'rgba(255,255,255,0.02)', border: '1px solid rgba(255,255,255,0.05)', fontFamily: 'var(--font-mono)', flexWrap: 'wrap', gap: '15px' }}>
                    <div>
                      <span style={{ fontSize: '1.1rem', color: '#fff', fontWeight: 'bold' }}>{u.fullName}</span>
                      <span style={{ marginLeft: '15px', color: 'var(--cp-text-muted)', fontSize: '0.85rem' }}>({u.email})</span>
                    </div>
                    
                    <div style={{ display: 'flex', gap: '20px', alignItems: 'center', flexWrap: 'wrap' }}>
                      <span style={{ color: u.role === 'CLIENT' ? 'var(--cp-cyan)' : u.role === 'EXPERT' ? 'var(--cp-magenta)' : '#ffcf00' }}>
                        ROLE: {u.role}
                      </span>
                      <span style={{ color: u.isLocked ? '#ff006e' : '#39ff14' }}>
                        STATUS: {u.isLocked ? 'LOCKED 🔒' : 'ACTIVE 🔓'}
                      </span>
                      {u.role !== 'ADMIN' && (
                        <button 
                          onClick={() => toggleUserStatus(u.id)} 
                          className="btn btn-outline" 
                          style={{ fontSize: '0.75rem', padding: '6px 12px', borderColor: u.isLocked ? '#39ff14' : '#ff006e', color: u.isLocked ? '#39ff14' : '#ff006e' }}
                        >
                          {u.isLocked ? 'MỞ KHÓA TÀI KHOẢN' : 'KHÓA TÀI KHOẢN'}
                        </button>
                      )}
                    </div>
                  </div>
                ))}
              </div>
            )}
          </div>
        )}

        {/* Tab 3: Content Management */}
        {activeTab === 'content' && (
          <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(320px, 1fr))', gap: '30px' }}>
            {/* Jobs Management */}
            <div className="cta-box" style={{ padding: '30px', border: '1px solid rgba(0,240,255,0.15)', background: 'rgba(5,5,10,0.85)' }}>
              <h2 style={{ fontFamily: 'var(--font-orbitron)', fontSize: '1.2rem', color: 'var(--cp-cyan)', borderBottom: '1px dashed rgba(0,240,255,0.2)', paddingBottom: '10px', marginBottom: '20px' }}>
                QUẢN LÝ BÀI ĐĂNG TUYỂN DỤNG (JOBS)
              </h2>
              {loadingContent ? (
                <div style={{ color: 'var(--cp-cyan)', fontFamily: 'var(--font-mono)' }}>⚡ ĐANG TẢI...</div>
              ) : jobs.length === 0 ? (
                <p style={{ color: 'var(--cp-text-muted)', fontFamily: 'var(--font-mono)' }}>// Không có bài đăng nào.</p>
              ) : (
                <div style={{ display: 'flex', flexDirection: 'column', gap: '15px' }}>
                  {jobs.map(j => (
                    <div key={j.id} style={{ padding: '15px', background: 'rgba(255,255,255,0.02)', border: '1px solid rgba(255,255,255,0.05)' }}>
                      <h4 style={{ margin: '0 0 5px 0', color: '#fff' }}>{j.title}</h4>
                      <p style={{ margin: '0 0 10px 0', fontSize: '0.8rem', color: 'var(--cp-text-muted)', fontFamily: 'var(--font-mono)' }}>
                        Ngân sách: {j.budgetMin?.toLocaleString()} - {j.budgetMax?.toLocaleString()} VND | Trạng thái: {j.status}
                      </p>
                      <button 
                         onClick={() => handleDeleteJob(j.id)}
                        className="btn btn-outline"
                        style={{ fontSize: '0.75rem', padding: '4px 10px', borderColor: '#ff006e', color: '#ff006e' }}
                      >
                        XÓA BÀI ĐĂNG
                      </button>
                    </div>
                  ))}
                </div>
              )}
            </div>

            {/* Services Management */}
            <div className="cta-box" style={{ padding: '30px', border: '1px solid rgba(176,38,255,0.15)', background: 'rgba(5,5,10,0.85)' }}>
              <h2 style={{ fontFamily: 'var(--font-orbitron)', fontSize: '1.2rem', color: 'var(--cp-magenta)', borderBottom: '1px dashed rgba(176,38,255,0.2)', paddingBottom: '10px', marginBottom: '20px' }}>
                QUẢN LÝ GÓI DỊCH VỤ MARKETPLACE (SERVICES)
              </h2>
              {loadingContent ? (
                <div style={{ color: 'var(--cp-cyan)', fontFamily: 'var(--font-mono)' }}>⚡ ĐANG TẢI...</div>
              ) : services.length === 0 ? (
                <p style={{ color: 'var(--cp-text-muted)', fontFamily: 'var(--font-mono)' }}>// Không có dịch vụ nào.</p>
              ) : (
                <div style={{ display: 'flex', flexDirection: 'column', gap: '15px' }}>
                  {services.map(s => (
                    <div key={s.id} style={{ padding: '15px', background: 'rgba(255,255,255,0.02)', border: '1px solid rgba(255,255,255,0.05)' }}>
                      <h4 style={{ margin: '0 0 5px 0', color: '#fff' }}>{s.title}</h4>
                      <p style={{ margin: '0 0 10px 0', fontSize: '0.8rem', color: 'var(--cp-text-muted)', fontFamily: 'var(--font-mono)' }}>
                        Giá: {s.price?.toLocaleString()} VND | Chuyên môn: {s.category}
                      </p>
                      <button 
                        onClick={() => handleDeleteService(s.id)}
                        className="btn btn-outline"
                        style={{ fontSize: '0.75rem', padding: '4px 10px', borderColor: '#ff006e', color: '#ff006e' }}
                      >
                        GỠ DỊCH VỤ
                      </button>
                    </div>
                  ))}
                </div>
              )}
            </div>

            {/* Reviews Management */}
            <div className="cta-box" style={{ padding: '30px', border: '1px solid rgba(57,255,20,0.15)', background: 'rgba(5,5,10,0.85)' }}>
              <h2 style={{ fontFamily: 'var(--font-orbitron)', fontSize: '1.2rem', color: '#39ff14', borderBottom: '1px dashed rgba(57,255,20,0.2)', paddingBottom: '10px', marginBottom: '20px' }}>
                QUẢN LÝ ĐÁNH GIÁ (REVIEWS)
              </h2>
              {loadingContent ? (
                <div style={{ color: 'var(--cp-cyan)', fontFamily: 'var(--font-mono)' }}>⚡ ĐANG TẢI...</div>
              ) : reviews.length === 0 ? (
                <p style={{ color: 'var(--cp-text-muted)', fontFamily: 'var(--font-mono)' }}>// Không có đánh giá nào.</p>
              ) : (
                <div style={{ display: 'flex', flexDirection: 'column', gap: '15px' }}>
                  {reviews.map(r => (
                    <div key={r.id} style={{ padding: '15px', background: 'rgba(255,255,255,0.02)', border: '1px solid rgba(255,255,255,0.05)' }}>
                      <div style={{ display: 'flex', justifyContent: 'space-between', marginBottom: '5px' }}>
                        <span style={{ fontSize: '0.8rem', color: 'var(--cp-cyan)', fontFamily: 'var(--font-mono)' }}>
                          {r.reviewerName} ➔ {r.revieweeName}
                        </span>
                        <span style={{ fontSize: '0.85rem', color: '#ffcf00' }}>{r.rating} ⭐</span>
                      </div>
                      <p style={{ margin: '0 0 10px 0', fontSize: '0.9rem', color: '#fff', fontStyle: 'italic' }}>
                        "{r.comment}"
                      </p>
                      <button 
                        onClick={() => handleDeleteReview(r.id)}
                        className="btn btn-outline"
                        style={{ fontSize: '0.75rem', padding: '4px 10px', borderColor: '#ff006e', color: '#ff006e' }}
                      >
                        XÓA ĐÁNH GIÁ
                      </button>
                    </div>
                  ))}
                </div>
              )}
            </div>
          </div>
        )}

        {/* Tab 4: Dispute Resolution */}
        {activeTab === 'disputes' && (
          <div className="cta-box" style={{ padding: '30px', border: '1px solid rgba(176,38,255,0.15)', background: 'rgba(5,5,10,0.85)' }}>
            <h2 style={{ fontFamily: 'var(--font-orbitron)', fontSize: '1.3rem', color: 'var(--cp-magenta)', borderBottom: '1px dashed rgba(176,38,255,0.2)', paddingBottom: '10px', marginBottom: '20px' }}>
              XỬ LÝ KHIẾU NẠI & HOÀN TIỀN KÝ QUỸ (DISPUTE WORKFLOW)
            </h2>
            {loadingDisputes ? (
              <div style={{ color: 'var(--cp-cyan)', fontFamily: 'var(--font-mono)' }}>⚡ ĐANG TẢI TRANH CHẤP...</div>
            ) : disputes.length === 0 ? (
              <p style={{ color: 'var(--cp-text-muted)', fontFamily: 'var(--font-mono)' }}>
                // HIỆN TẠI KHÔNG CÓ KHIẾU NẠI TRANH CHẤP NÀO TRÊN HỆ THỐNG.
              </p>
            ) : (
              <div style={{ display: 'flex', flexDirection: 'column', gap: '15px' }}>
                {disputes.map(d => (
                  <div key={d.id} style={{ padding: '20px', background: 'rgba(255,255,255,0.02)', border: '1px solid rgba(255,255,255,0.05)' }}>
                    <div style={{ display: 'flex', justifyContent: 'space-between', marginBottom: '10px', fontFamily: 'var(--font-mono)', fontSize: '0.85rem' }}>
                      <span style={{ color: 'var(--cp-cyan)' }}>⚖️ DỰ ÁN: {d.title}</span>
                      <span style={{ color: 'var(--cp-magenta)' }}>SỐ TIỀN TRANH CHẤP: {d.amount?.toLocaleString()} VND</span>
                    </div>
                    <div style={{ fontFamily: 'var(--font-mono)', fontSize: '0.8rem', color: 'var(--cp-text-muted)', marginBottom: '15px' }}>
                      <span>Client: {d.clientName}</span> | <span>Expert: {d.expertName}</span> | <span>Lý do: {d.reason}</span>
                    </div>

                    <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
                      <span style={{ fontFamily: 'var(--font-mono)', fontSize: '0.8rem', color: d.status === 'PENDING' ? '#ffcf00' : '#39ff14' }}>
                        TRẠNG THÁI: {d.status}
                      </span>
                      {d.status === 'PENDING' && (
                        <div style={{ display: 'flex', gap: '10px' }}>
                          <button 
                            onClick={() => handleResolveDispute(d.id, 'REFUND')} 
                            className="btn btn-primary" 
                            style={{ fontSize: '0.75rem', padding: '6px 12px' }}
                          >
                            <span>APPROVED REFUND (HOÀN TIỀN CHO CLIENT)</span>
                          </button>
                          <button 
                            onClick={() => handleResolveDispute(d.id, 'RELEASE')} 
                            className="btn btn-outline" 
                            style={{ fontSize: '0.75rem', padding: '6px 12px', borderColor: '#39ff14', color: '#39ff14' }}
                          >
                            <span>FORCE RELEASE (GIẢI NGÂN CHO EXPERT)</span>
                          </button>
                        </div>
                      )}
                    </div>
                  </div>
                ))}
              </div>
            )}
          </div>
        )}

        {/* Tab 5: Withdrawals Management */}
        {activeTab === 'withdrawals' && (
          <div className="cta-box" style={{ padding: '30px', border: '1px solid var(--cp-magenta)', background: 'rgba(5,5,10,0.85)' }}>
            <h2 style={{ fontFamily: 'var(--font-orbitron)', fontSize: '1.3rem', color: 'var(--cp-magenta)', borderBottom: '1px dashed rgba(176,38,255,0.2)', paddingBottom: '10px', marginBottom: '20px' }}>
              PHÊ DUYỆT YÊU CẦU RÚT TIỀN (WITHDRAWALS REVIEW)
            </h2>
            
            {loadingWithdrawals ? (
              <div style={{ color: 'var(--cp-cyan)', fontFamily: 'var(--font-mono)', padding: '20px' }}>
                ⚡ ĐANG TẢI CÁC YÊU CẦU RÚT TIỀN...
              </div>
            ) : withdrawals.length === 0 ? (
              <p style={{ color: 'var(--cp-text-muted)', fontFamily: 'var(--font-mono)' }}>
                // HIỆN TẠI KHÔNG CÓ YÊU CẦU RÚT TIỀN NÀO TRÊN HỆ THỐNG.
              </p>
            ) : (
              <div style={{ display: 'flex', flexDirection: 'column', gap: '15px' }}>
                {withdrawals.map(w => (
                  <div key={w.id} style={{ padding: '20px', background: 'rgba(255,255,255,0.02)', border: '1px solid rgba(255,255,255,0.05)' }}>
                    <div style={{ display: 'flex', justifyContent: 'space-between', marginBottom: '10px', fontFamily: 'var(--font-mono)', fontSize: '0.85rem' }}>
                      <span style={{ color: 'var(--cp-cyan)' }}>👨‍💻 CHUYÊN GIA: {w.userFullName} ({w.userEmail})</span>
                      <span style={{ color: 'var(--cp-magenta)', fontWeight: 'bold' }}>SỐ TIỀN RÚT: {w.amount?.toLocaleString()} VND</span>
                    </div>
                    <div style={{ fontFamily: 'var(--font-mono)', fontSize: '0.8rem', color: 'var(--cp-text-muted)', marginBottom: '15px' }}>
                      <span>Ngân hàng: {w.bankName}</span> | <span>Số tài khoản: {w.accountNumber}</span> | <span>Tên chủ thẻ: {w.accountHolderName}</span> | <span>Thời gian: {new Date(w.createdAt).toLocaleString()}</span>
                    </div>

                    <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
                      <span style={{ 
                        fontFamily: 'var(--font-mono)', 
                        fontSize: '0.85rem',
                        color: w.status === 'APPROVED' ? '#39ff14' : w.status === 'REJECTED' ? '#ff006e' : '#ffcf00'
                      }}>
                        TRẠNG THÁI: {w.status}
                      </span>
                      
                      {w.status === 'PENDING' && (
                        <div style={{ display: 'flex', gap: '10px' }}>
                          <button 
                            onClick={() => handleApproveWithdrawal(w.id)} 
                            className="btn btn-primary" 
                            style={{ fontSize: '0.75rem', padding: '6px 12px' }}
                          >
                            <span>DUYỆT & CHUYỂN TIỀN</span>
                          </button>
                          <button 
                            onClick={() => handleRejectWithdrawal(w.id)} 
                            className="btn btn-outline" 
                            style={{ fontSize: '0.75rem', padding: '6px 12px', borderColor: '#ff006e', color: '#ff006e' }}
                          >
                            <span>TỪ CHỐI</span>
                          </button>
                        </div>
                      )}
                    </div>
                  </div>
                ))}
              </div>
            )}
          </div>
        )}

      </div>
    </div>
  );
}
