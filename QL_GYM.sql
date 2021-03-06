USE [master]
GO
/****** Object:  Database [QL_GYM]    Script Date: 12/9/2021 5:58:49 PM ******/
CREATE DATABASE [QL_GYM]
 CONTAINMENT = NONE
 ON  PRIMARY 
( NAME = N'QL_GYM', FILENAME = N'E:\SQL Server\Project\QL_GYM.mdf' , SIZE = 8192KB , MAXSIZE = UNLIMITED, FILEGROWTH = 65536KB )
 LOG ON 
( NAME = N'QL_GYM_log', FILENAME = N'E:\SQL Server\Project\QL_GYM_log.ldf' , SIZE = 8192KB , MAXSIZE = 2048GB , FILEGROWTH = 65536KB )
GO
ALTER DATABASE [QL_GYM] SET COMPATIBILITY_LEVEL = 120
GO
IF (1 = FULLTEXTSERVICEPROPERTY('IsFullTextInstalled'))
begin
EXEC [QL_GYM].[dbo].[sp_fulltext_database] @action = 'enable'
end
GO
ALTER DATABASE [QL_GYM] SET ANSI_NULL_DEFAULT OFF 
GO
ALTER DATABASE [QL_GYM] SET ANSI_NULLS OFF 
GO
ALTER DATABASE [QL_GYM] SET ANSI_PADDING OFF 
GO
ALTER DATABASE [QL_GYM] SET ANSI_WARNINGS OFF 
GO
ALTER DATABASE [QL_GYM] SET ARITHABORT OFF 
GO
ALTER DATABASE [QL_GYM] SET AUTO_CLOSE OFF 
GO
ALTER DATABASE [QL_GYM] SET AUTO_SHRINK OFF 
GO
ALTER DATABASE [QL_GYM] SET AUTO_UPDATE_STATISTICS ON 
GO
ALTER DATABASE [QL_GYM] SET CURSOR_CLOSE_ON_COMMIT OFF 
GO
ALTER DATABASE [QL_GYM] SET CURSOR_DEFAULT  GLOBAL 
GO
ALTER DATABASE [QL_GYM] SET CONCAT_NULL_YIELDS_NULL OFF 
GO
ALTER DATABASE [QL_GYM] SET NUMERIC_ROUNDABORT OFF 
GO
ALTER DATABASE [QL_GYM] SET QUOTED_IDENTIFIER OFF 
GO
ALTER DATABASE [QL_GYM] SET RECURSIVE_TRIGGERS OFF 
GO
ALTER DATABASE [QL_GYM] SET  DISABLE_BROKER 
GO
ALTER DATABASE [QL_GYM] SET AUTO_UPDATE_STATISTICS_ASYNC OFF 
GO
ALTER DATABASE [QL_GYM] SET DATE_CORRELATION_OPTIMIZATION OFF 
GO
ALTER DATABASE [QL_GYM] SET TRUSTWORTHY OFF 
GO
ALTER DATABASE [QL_GYM] SET ALLOW_SNAPSHOT_ISOLATION OFF 
GO
ALTER DATABASE [QL_GYM] SET PARAMETERIZATION SIMPLE 
GO
ALTER DATABASE [QL_GYM] SET READ_COMMITTED_SNAPSHOT OFF 
GO
ALTER DATABASE [QL_GYM] SET HONOR_BROKER_PRIORITY OFF 
GO
ALTER DATABASE [QL_GYM] SET RECOVERY FULL 
GO
ALTER DATABASE [QL_GYM] SET  MULTI_USER 
GO
ALTER DATABASE [QL_GYM] SET PAGE_VERIFY CHECKSUM  
GO
ALTER DATABASE [QL_GYM] SET DB_CHAINING OFF 
GO
ALTER DATABASE [QL_GYM] SET FILESTREAM( NON_TRANSACTED_ACCESS = OFF ) 
GO
ALTER DATABASE [QL_GYM] SET TARGET_RECOVERY_TIME = 60 SECONDS 
GO
ALTER DATABASE [QL_GYM] SET DELAYED_DURABILITY = DISABLED 
GO
EXEC sys.sp_db_vardecimal_storage_format N'QL_GYM', N'ON'
GO
USE [QL_GYM]
GO
/****** Object:  Table [dbo].[DIEMDANH]    Script Date: 12/9/2021 5:58:49 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[DIEMDANH](
	[MaDD] [varchar](255) NOT NULL,
	[SoLan] [int] NULL,
	[ThoiGian] [datetime] NULL,
	[maThe] [varchar](255) NULL,
PRIMARY KEY CLUSTERED 
(
	[MaDD] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[GOITAP]    Script Date: 12/9/2021 5:58:49 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[GOITAP](
	[MaGoiTap] [nvarchar](50) NOT NULL,
	[MaLop] [nvarchar](50) NOT NULL,
	[TenGoiTap] [nvarchar](200) NULL,
	[ThoiHan] [int] NULL,
	[Gia] [float] NULL,
	[TrangThai] [int] NULL CONSTRAINT [DF_GOITAP_TrangThai]  DEFAULT ((1)),
 CONSTRAINT [PK_GOITAP_1] PRIMARY KEY CLUSTERED 
(
	[MaGoiTap] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO
/****** Object:  Table [dbo].[HOADON]    Script Date: 12/9/2021 5:58:49 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[HOADON](
	[MasoHD] [nvarchar](50) NOT NULL,
	[NgayHD] [date] NULL,
	[MaThe] [nvarchar](50) NULL,
	[MaNV] [nvarchar](50) NULL,
 CONSTRAINT [PK_HOADON] PRIMARY KEY CLUSTERED 
(
	[MasoHD] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO
/****** Object:  Table [dbo].[KHACHHANG]    Script Date: 12/9/2021 5:58:49 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[KHACHHANG](
	[MaKH] [nvarchar](50) NOT NULL,
	[TenKH] [nvarchar](100) NOT NULL,
	[DiaChi] [nvarchar](200) NULL,
	[Email] [nvarchar](100) NOT NULL,
	[SDT] [char](10) NULL,
	[NgaySinh] [date] NULL,
	[GioiTinh] [nvarchar](50) NULL,
	[Anh] [text] NULL,
	[GhiChu] [varchar](255) NULL,
 CONSTRAINT [PK_KHACHHANG] PRIMARY KEY CLUSTERED 
(
	[MaKH] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]

GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[LOPDV]    Script Date: 12/9/2021 5:58:49 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[LOPDV](
	[MaLop] [nvarchar](50) NOT NULL,
	[TenLop] [nvarchar](200) NULL,
	[TrangThai] [int] NULL,
 CONSTRAINT [PK_LOPDV] PRIMARY KEY CLUSTERED 
(
	[MaLop] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO
/****** Object:  Table [dbo].[NHANVIEN]    Script Date: 12/9/2021 5:58:49 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[NHANVIEN](
	[MaNV] [nvarchar](50) NOT NULL,
	[TenNV] [nvarchar](50) NOT NULL,
	[Email] [nvarchar](50) NOT NULL,
	[SDT] [nvarchar](11) NULL,
	[DiaChi] [nvarchar](200) NOT NULL,
	[GioiTinh] [nvarchar](50) NOT NULL,
	[Username] [nvarchar](50) NOT NULL,
	[SĐT] [varchar](255) NULL,
 CONSTRAINT [PK_NHANVIEN] PRIMARY KEY CLUSTERED 
(
	[MaNV] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[NHANVIEN_HOADON]    Script Date: 12/9/2021 5:58:49 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[NHANVIEN_HOADON](
	[NhanVien_MaNV] [varchar](255) NOT NULL,
	[hoaDons_MasoHD] [varchar](255) NOT NULL
) ON [PRIMARY]

GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[PHANQUYEN]    Script Date: 12/9/2021 5:58:49 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[PHANQUYEN](
	[MaQuyen] [int] NOT NULL,
	[ChucVu] [nvarchar](100) NULL,
 CONSTRAINT [PK_PHANQUYEN] PRIMARY KEY CLUSTERED 
(
	[MaQuyen] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO
/****** Object:  Table [dbo].[PHANQUYEN_TAIKHOAN]    Script Date: 12/9/2021 5:58:49 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[PHANQUYEN_TAIKHOAN](
	[PhanQuyen_MaQuyen] [int] NOT NULL,
	[taiKhoans_Username] [varchar](255) NOT NULL
) ON [PRIMARY]

GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[TAIKHOAN]    Script Date: 12/9/2021 5:58:49 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[TAIKHOAN](
	[Username] [nvarchar](50) NOT NULL,
	[Password] [nvarchar](50) NULL,
	[MaQuyen] [int] NULL,
	[TrangThai] [int] NULL,
 CONSTRAINT [PK_TAIKHOAN] PRIMARY KEY CLUSTERED 
(
	[Username] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO
/****** Object:  Table [dbo].[THE]    Script Date: 12/9/2021 5:58:49 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[THE](
	[MaThe] [nvarchar](50) NOT NULL,
	[MaKH] [nvarchar](50) NOT NULL,
	[MaGoiTap] [nvarchar](50) NOT NULL,
	[NgayDK] [date] NOT NULL,
	[NgayBD] [date] NULL,
	[NgayKT] [date] NULL,
	[Trangthai] [nvarchar](50) NOT NULL,
 CONSTRAINT [PK_THE] PRIMARY KEY CLUSTERED 
(
	[MaThe] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO
INSERT [dbo].[GOITAP] ([MaGoiTap], [MaLop], [TenGoiTap], [ThoiHan], [Gia], [TrangThai]) VALUES (N'GTN1', N'BX1', N'năm', 365, 5400000, NULL)
INSERT [dbo].[GOITAP] ([MaGoiTap], [MaLop], [TenGoiTap], [ThoiHan], [Gia], [TrangThai]) VALUES (N'GTN2', N'YG2', N'năm', 365, 7200000, NULL)
INSERT [dbo].[GOITAP] ([MaGoiTap], [MaLop], [TenGoiTap], [ThoiHan], [Gia], [TrangThai]) VALUES (N'GTN3', N'EB3', N'năm', 365, 7200000, NULL)
INSERT [dbo].[GOITAP] ([MaGoiTap], [MaLop], [TenGoiTap], [ThoiHan], [Gia], [TrangThai]) VALUES (N'GTN4', N'FN4', N'năm', 365, 5400000, NULL)
INSERT [dbo].[GOITAP] ([MaGoiTap], [MaLop], [TenGoiTap], [ThoiHan], [Gia], [TrangThai]) VALUES (N'GTNG1', N'BX1', N'ngày', 1, 15000, NULL)
INSERT [dbo].[GOITAP] ([MaGoiTap], [MaLop], [TenGoiTap], [ThoiHan], [Gia], [TrangThai]) VALUES (N'GTNG2', N'YG2', N'ngày', 1, 20000, NULL)
INSERT [dbo].[GOITAP] ([MaGoiTap], [MaLop], [TenGoiTap], [ThoiHan], [Gia], [TrangThai]) VALUES (N'GTNG3', N'EB3', N'ngày', 1, 20000, NULL)
INSERT [dbo].[GOITAP] ([MaGoiTap], [MaLop], [TenGoiTap], [ThoiHan], [Gia], [TrangThai]) VALUES (N'GTNG4', N'FN4', N'ngày', 1, 15000, NULL)
INSERT [dbo].[GOITAP] ([MaGoiTap], [MaLop], [TenGoiTap], [ThoiHan], [Gia], [TrangThai]) VALUES (N'GTNG5', N'GM5', N'1 ngày', 1, 200000, 1)
INSERT [dbo].[GOITAP] ([MaGoiTap], [MaLop], [TenGoiTap], [ThoiHan], [Gia], [TrangThai]) VALUES (N'GTT1', N'BX1', N'tuần', 7, 100000, NULL)
INSERT [dbo].[GOITAP] ([MaGoiTap], [MaLop], [TenGoiTap], [ThoiHan], [Gia], [TrangThai]) VALUES (N'GTT2', N'YG2', N'tuần', 7, 130000, NULL)
INSERT [dbo].[GOITAP] ([MaGoiTap], [MaLop], [TenGoiTap], [ThoiHan], [Gia], [TrangThai]) VALUES (N'GTT3', N'EB3', N'tuần', 7, 130000, NULL)
INSERT [dbo].[GOITAP] ([MaGoiTap], [MaLop], [TenGoiTap], [ThoiHan], [Gia], [TrangThai]) VALUES (N'GTT4', N'FN4', N'tuần', 7, 100000, NULL)
INSERT [dbo].[GOITAP] ([MaGoiTap], [MaLop], [TenGoiTap], [ThoiHan], [Gia], [TrangThai]) VALUES (N'GTTH1', N'BX1', N'tháng', 30, 400000, NULL)
INSERT [dbo].[GOITAP] ([MaGoiTap], [MaLop], [TenGoiTap], [ThoiHan], [Gia], [TrangThai]) VALUES (N'GTTH2', N'YG2', N'tháng', 30, 550000, NULL)
INSERT [dbo].[GOITAP] ([MaGoiTap], [MaLop], [TenGoiTap], [ThoiHan], [Gia], [TrangThai]) VALUES (N'GTTH3', N'EB3', N'tháng', 30, 550000, NULL)
INSERT [dbo].[HOADON] ([MasoHD], [NgayHD], [MaThe], [MaNV]) VALUES (N'HD1', CAST(N'2019-02-20' AS Date), N'T1', N'NV1')
INSERT [dbo].[HOADON] ([MasoHD], [NgayHD], [MaThe], [MaNV]) VALUES (N'HD2', CAST(N'2021-01-02' AS Date), N'T2', N'NV1')
INSERT [dbo].[HOADON] ([MasoHD], [NgayHD], [MaThe], [MaNV]) VALUES (N'HD3', CAST(N'2021-01-25' AS Date), N'T3', N'NV1')
INSERT [dbo].[HOADON] ([MasoHD], [NgayHD], [MaThe], [MaNV]) VALUES (N'HD4', CAST(N'2021-02-20' AS Date), N'T4', N'NV1')
INSERT [dbo].[HOADON] ([MasoHD], [NgayHD], [MaThe], [MaNV]) VALUES (N'HD5', CAST(N'2021-11-24' AS Date), N'TT1', N'NV1')
INSERT [dbo].[KHACHHANG] ([MaKH], [TenKH], [DiaChi], [Email], [SDT], [NgaySinh], [GioiTinh], [Anh], [GhiChu]) VALUES (N'KH 5', N'Lê Văn Quý', N'TP HCM', N'lvquy@gmail.com', N'0352332343', NULL, N'Nam', N'KH 5.jpg', NULL)
INSERT [dbo].[KHACHHANG] ([MaKH], [TenKH], [DiaChi], [Email], [SDT], [NgaySinh], [GioiTinh], [Anh], [GhiChu]) VALUES (N'KH1', N'Hồ Lê', N'Thanh Hóa', N'hl@gmail.com', N'0265909276', NULL, N'Nam', N'KH1_H? Lê.png', NULL)
INSERT [dbo].[KHACHHANG] ([MaKH], [TenKH], [DiaChi], [Email], [SDT], [NgaySinh], [GioiTinh], [Anh], [GhiChu]) VALUES (N'KH2', N'Bùi Bích Trâm', N'Man Thiện, tp Thủ Đức', N'trambb@gmail.com', N'0365855455', CAST(N'2001-04-07' AS Date), N'Nữ', N'KH2_Bùi Bích Trâm.jpg', NULL)
INSERT [dbo].[KHACHHANG] ([MaKH], [TenKH], [DiaChi], [Email], [SDT], [NgaySinh], [GioiTinh], [Anh], [GhiChu]) VALUES (N'KH3', N'Nguyễn Văn Tiến', N'Nguyễn Bính, quận 11', N'tienn@gmail.com', N'0289554554', CAST(N'1999-03-04' AS Date), N'Nữ', N'KH3.jpg', NULL)
INSERT [dbo].[KHACHHANG] ([MaKH], [TenKH], [DiaChi], [Email], [SDT], [NgaySinh], [GioiTinh], [Anh], [GhiChu]) VALUES (N'KH4', N'Đỗ Thị Tâm', N'Tôn Thắng, quận 7', N'tamdt@gmail.com', N'0352615030', CAST(N'2001-07-28' AS Date), N'Nữ', N'KH4_Ð? Th? Tâm.png', NULL)
INSERT [dbo].[LOPDV] ([MaLop], [TenLop], [TrangThai]) VALUES (N'BX1', N'Boxing', NULL)
INSERT [dbo].[LOPDV] ([MaLop], [TenLop], [TrangThai]) VALUES (N'EB3', N'Aerobic', NULL)
INSERT [dbo].[LOPDV] ([MaLop], [TenLop], [TrangThai]) VALUES (N'FN4', N'Fitness', NULL)
INSERT [dbo].[LOPDV] ([MaLop], [TenLop], [TrangThai]) VALUES (N'GM5', N'GYM', NULL)
INSERT [dbo].[LOPDV] ([MaLop], [TenLop], [TrangThai]) VALUES (N'YG2', N'Yoga', NULL)
INSERT [dbo].[NHANVIEN] ([MaNV], [TenNV], [Email], [SDT], [DiaChi], [GioiTinh], [Username], [SĐT]) VALUES (N'NV1', N'Đỗ Tâm', N'dt@gmail.com', N'0981640754', N'Man Thiện, tp Thủ Đức', N'Nữ', N'Tam', NULL)
INSERT [dbo].[NHANVIEN] ([MaNV], [TenNV], [Email], [SDT], [DiaChi], [GioiTinh], [Username], [SĐT]) VALUES (N'NV2', N'Chi', N'chi@gmail.com', N'0563405078', N'DakLak', N'Nữ', N'Chi', NULL)
INSERT [dbo].[NHANVIEN] ([MaNV], [TenNV], [Email], [SDT], [DiaChi], [GioiTinh], [Username], [SĐT]) VALUES (N'NV3', N'Lê Đỗ', N'ledo@gmail.com', NULL, N'TP HCM', N'Nữ', N'ledo', N'0350001020')
INSERT [dbo].[PHANQUYEN] ([MaQuyen], [ChucVu]) VALUES (0, N'Quản Lý')
INSERT [dbo].[PHANQUYEN] ([MaQuyen], [ChucVu]) VALUES (1, N'Nhân viên')
INSERT [dbo].[TAIKHOAN] ([Username], [Password], [MaQuyen], [TrangThai]) VALUES (N'Chi', N'123456', 0, 1)
INSERT [dbo].[TAIKHOAN] ([Username], [Password], [MaQuyen], [TrangThai]) VALUES (N'ledo', N'123456', 1, 1)
INSERT [dbo].[TAIKHOAN] ([Username], [Password], [MaQuyen], [TrangThai]) VALUES (N'root', N'123456', 0, 1)
INSERT [dbo].[TAIKHOAN] ([Username], [Password], [MaQuyen], [TrangThai]) VALUES (N'Tam', N'123456', 0, 1)
INSERT [dbo].[THE] ([MaThe], [MaKH], [MaGoiTap], [NgayDK], [NgayBD], [NgayKT], [Trangthai]) VALUES (N'T1', N'KH1', N'GTNG4', CAST(N'2019-02-20' AS Date), CAST(N'2019-02-20' AS Date), CAST(N'2019-02-21' AS Date), N'Hết Hạn')
INSERT [dbo].[THE] ([MaThe], [MaKH], [MaGoiTap], [NgayDK], [NgayBD], [NgayKT], [Trangthai]) VALUES (N'T2', N'KH2', N'GTT2', CAST(N'2021-01-02' AS Date), CAST(N'2021-01-02' AS Date), CAST(N'2021-01-09' AS Date), N'Hết Hạn')
INSERT [dbo].[THE] ([MaThe], [MaKH], [MaGoiTap], [NgayDK], [NgayBD], [NgayKT], [Trangthai]) VALUES (N'T3', N'KH3', N'GTT3', CAST(N'2021-01-25' AS Date), CAST(N'2021-01-26' AS Date), CAST(N'2021-02-02' AS Date), N'Hết Hạn')
INSERT [dbo].[THE] ([MaThe], [MaKH], [MaGoiTap], [NgayDK], [NgayBD], [NgayKT], [Trangthai]) VALUES (N'T4', N'KH4', N'GTTH1', CAST(N'2021-02-20' AS Date), CAST(N'2021-02-21' AS Date), CAST(N'2020-03-21' AS Date), N'Hết Hạn')
INSERT [dbo].[THE] ([MaThe], [MaKH], [MaGoiTap], [NgayDK], [NgayBD], [NgayKT], [Trangthai]) VALUES (N'TT1', N'KH1', N'GTNG5', CAST(N'2021-12-08' AS Date), NULL, NULL, N'Chưa Thanh Toán')
SET ANSI_PADDING ON

GO
/****** Object:  Index [Unique_HOADON]    Script Date: 12/9/2021 5:58:49 PM ******/
ALTER TABLE [dbo].[HOADON] ADD  CONSTRAINT [Unique_HOADON] UNIQUE NONCLUSTERED 
(
	[MaThe] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, SORT_IN_TEMPDB = OFF, IGNORE_DUP_KEY = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
GO
SET ANSI_PADDING ON

GO
/****** Object:  Index [UK_kjt6m60s40de8k310wpq5ben0]    Script Date: 12/9/2021 5:58:49 PM ******/
ALTER TABLE [dbo].[NHANVIEN_HOADON] ADD  CONSTRAINT [UK_kjt6m60s40de8k310wpq5ben0] UNIQUE NONCLUSTERED 
(
	[hoaDons_MasoHD] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, SORT_IN_TEMPDB = OFF, IGNORE_DUP_KEY = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
GO
SET ANSI_PADDING ON

GO
/****** Object:  Index [UK_sjig3ahb1dyevrwvxphio6miw]    Script Date: 12/9/2021 5:58:49 PM ******/
ALTER TABLE [dbo].[PHANQUYEN_TAIKHOAN] ADD  CONSTRAINT [UK_sjig3ahb1dyevrwvxphio6miw] UNIQUE NONCLUSTERED 
(
	[taiKhoans_Username] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, SORT_IN_TEMPDB = OFF, IGNORE_DUP_KEY = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
GO
ALTER TABLE [dbo].[GOITAP]  WITH CHECK ADD  CONSTRAINT [FK_GOITAP_LOPDV] FOREIGN KEY([MaLop])
REFERENCES [dbo].[LOPDV] ([MaLop])
ON UPDATE CASCADE
ON DELETE CASCADE
GO
ALTER TABLE [dbo].[GOITAP] CHECK CONSTRAINT [FK_GOITAP_LOPDV]
GO
ALTER TABLE [dbo].[GOITAP]  WITH CHECK ADD  CONSTRAINT [FKbp3vxj5xu5ondksb9w2atgfwb] FOREIGN KEY([MaLop])
REFERENCES [dbo].[LOPDV] ([MaLop])
GO
ALTER TABLE [dbo].[GOITAP] CHECK CONSTRAINT [FKbp3vxj5xu5ondksb9w2atgfwb]
GO
ALTER TABLE [dbo].[HOADON]  WITH CHECK ADD  CONSTRAINT [FKkl0ayrbddex894r4ar5yu5twp] FOREIGN KEY([MaNV])
REFERENCES [dbo].[NHANVIEN] ([MaNV])
GO
ALTER TABLE [dbo].[HOADON] CHECK CONSTRAINT [FKkl0ayrbddex894r4ar5yu5twp]
GO
ALTER TABLE [dbo].[HOADON]  WITH CHECK ADD  CONSTRAINT [FKq7uqpo4tmkqaujwevsvceaceq] FOREIGN KEY([MaThe])
REFERENCES [dbo].[THE] ([MaThe])
ON UPDATE CASCADE
GO
ALTER TABLE [dbo].[HOADON] CHECK CONSTRAINT [FKq7uqpo4tmkqaujwevsvceaceq]
GO
ALTER TABLE [dbo].[HOADON]  WITH CHECK ADD  CONSTRAINT [FKqhyfkqp396i0yg8v350gibv7h] FOREIGN KEY([MaNV])
REFERENCES [dbo].[NHANVIEN] ([MaNV])
ON UPDATE CASCADE
GO
ALTER TABLE [dbo].[HOADON] CHECK CONSTRAINT [FKqhyfkqp396i0yg8v350gibv7h]
GO
ALTER TABLE [dbo].[NHANVIEN]  WITH CHECK ADD  CONSTRAINT [FKrsqx1rue7hw8m76m7ww4xebvf] FOREIGN KEY([Username])
REFERENCES [dbo].[TAIKHOAN] ([Username])
ON UPDATE CASCADE
GO
ALTER TABLE [dbo].[NHANVIEN] CHECK CONSTRAINT [FKrsqx1rue7hw8m76m7ww4xebvf]
GO
ALTER TABLE [dbo].[PHANQUYEN_TAIKHOAN]  WITH CHECK ADD  CONSTRAINT [FK14knb1xye2y84d2e9rirmmbm4] FOREIGN KEY([PhanQuyen_MaQuyen])
REFERENCES [dbo].[PHANQUYEN] ([MaQuyen])
ON UPDATE CASCADE
GO
ALTER TABLE [dbo].[PHANQUYEN_TAIKHOAN] CHECK CONSTRAINT [FK14knb1xye2y84d2e9rirmmbm4]
GO
ALTER TABLE [dbo].[TAIKHOAN]  WITH CHECK ADD  CONSTRAINT [FKepry8ikqfop2ylr5k5leotdiy] FOREIGN KEY([MaQuyen])
REFERENCES [dbo].[PHANQUYEN] ([MaQuyen])
ON UPDATE CASCADE
GO
ALTER TABLE [dbo].[TAIKHOAN] CHECK CONSTRAINT [FKepry8ikqfop2ylr5k5leotdiy]
GO
ALTER TABLE [dbo].[THE]  WITH CHECK ADD  CONSTRAINT [FKahfpy15g1iunufoxvmmbjqggx] FOREIGN KEY([MaKH])
REFERENCES [dbo].[KHACHHANG] ([MaKH])
ON UPDATE CASCADE
GO
ALTER TABLE [dbo].[THE] CHECK CONSTRAINT [FKahfpy15g1iunufoxvmmbjqggx]
GO
ALTER TABLE [dbo].[THE]  WITH CHECK ADD  CONSTRAINT [FKgq0hi0bc3ylplutw23x3n3wyp] FOREIGN KEY([MaGoiTap])
REFERENCES [dbo].[GOITAP] ([MaGoiTap])
ON UPDATE CASCADE
ON DELETE CASCADE
GO
ALTER TABLE [dbo].[THE] CHECK CONSTRAINT [FKgq0hi0bc3ylplutw23x3n3wyp]
GO
ALTER TABLE [dbo].[GOITAP]  WITH CHECK ADD  CONSTRAINT [CK_GOITAP_TRANGTHAI] CHECK  (([TrangThai]=(1) OR [TrangThai]=(0)))
GO
ALTER TABLE [dbo].[GOITAP] CHECK CONSTRAINT [CK_GOITAP_TRANGTHAI]
GO
ALTER TABLE [dbo].[TAIKHOAN]  WITH CHECK ADD  CONSTRAINT [CK_TrangThai] CHECK  (([TrangThai]=(1) OR [TrangThai]=(0)))
GO
ALTER TABLE [dbo].[TAIKHOAN] CHECK CONSTRAINT [CK_TrangThai]
GO
USE [master]
GO
ALTER DATABASE [QL_GYM] SET  READ_WRITE 
GO
