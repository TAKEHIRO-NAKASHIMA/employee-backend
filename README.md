# 社員管理システム

## 概要

Spring Boot + React を使用した社員管理システムです。

## 使用技術

### バックエンド
- Java 21
- Spring Boot 4
- Spring Security
- JWT
- JPA (Hibernate)

### フロントエンド
- React
- TypeScript
- Vite

### データベース
- PostgreSQL

### インフラ・開発環境
- Docker
- Render
- Vercel

### テスト
- JUnit 5
- Mockito
- MockMvc

## 主な機能

- JWT認証によるログイン機能
- 社員情報の登録・一覧取得・詳細取得・更新・削除（CRUD）
- 名前・メールアドレス・役職による検索機能
- ページネーション機能
- ソート機能
- バリデーション機能
- GlobalExceptionHandlerによる例外処理
- Axios interceptor を使用した認証管理
- PrivateRoute による認証制御
  
## テスト

JUnit・Mockito・MockMvc を使用してテストを実施しました。

### Service層
- findById 正常系 / 異常系
- create
- update 正常系 / 異常系
- delete 正常系 / 異常系

### Controller層
- GET 正常系 / 異常系
- POST 正常系 / バリデーション異常系
- PUT 正常系 / 異常系
- DELETE 正常系 / 異常系
- 一覧取得API（ページネーション付き）


## API一覧

## 起動方法

### バックエンド

```bash
cd backend
./mvnw spring-boot:run

### フロントエンド
cd frontend
npm install
npm run dev

### Docker(postgreSQL)
docker compose up -d

## 環境変数
backend/src/main/resources/application.yml に設定

## デプロイURL

### フロントエンド
https://employee-frontend-omega-swart.vercel.app/

### バックエンド
https://employee-backend-4f5f.onrender.com

### テスト用アカウント

- Email: test@example.com
- Password: password
