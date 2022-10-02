# NotesApp

## Sản phẩm Notes App Android bằng ngôn ngữ Kotlin sau khoá học cơ bản về Mobile tại ProPTIT.

## Những tính năng đã làm được và những kiến thức sử dụng
* Giao diện chính với Button Navigation để chuyển các fragment
* Sử dụng Room Database và LiveData cho các thao tác tạo mới, sửa, xoá Notes
* Sử dụng Recycler View để hiển thị các Notes
* Notes có title, description, time, date
* Có tính năng sort note theo id, title và timeremain
* Có tính năng search note by title
* Có sử dụng các Dialog để thông báo
* Swipe note để xoá hoặc recover note
* Sử dụng các interface để tối ưu code

## Mô tả chi tiết về sản phẩm:
* Giao diện chính của App

  <img src="https://user-images.githubusercontent.com/84316258/193429404-238fa076-71b0-464d-9c8f-965765571a5e.jpg" width="300" />
* Giao diện thêm mới Note
  
  <img src="https://user-images.githubusercontent.com/84316258/193429538-7d3f88ae-8fda-420c-b187-64ba90c27de9.jpg" width="300" />
* Click vào bảng chọn màu cho note background

  <img src="https://user-images.githubusercontent.com/84316258/193429587-09210a76-befe-4a19-aac9-24efe19058c6.jpg" width="200" />
* Chọn deadline cho Note:

  <img src="https://user-images.githubusercontent.com/84316258/193429641-62b7c715-f3fc-4d1b-95a4-2ca42e9d7a3d.jpg" width="200" />
  <img src="https://user-images.githubusercontent.com/84316258/193429645-aadbb08e-0975-4b04-99c0-50e6369f30bd.jpg" width="200" />
* Nếu có phương thức nào chưa được chọn đủ thì sẽ không lưu được

  <img src="https://user-images.githubusercontent.com/84316258/193429697-7b806b57-564b-4090-833b-2b12aa205d70.jpg" width="300" />
  <img src="https://user-images.githubusercontent.com/84316258/193429706-e7ae6204-b889-4cd3-9fbc-0ad0cf79ab85.jpg" width="300" />
  
* Màn hình chính khi hiển thị các Note:

  <img src="https://user-images.githubusercontent.com/84316258/193429802-eb0ce1bf-e657-4861-9a32-eb08f33429c2.jpg" width="300" />

* Màn hình Calender hiển thị các công việc có trong ngày theo thời gian tăng dần

  <img src="https://user-images.githubusercontent.com/84316258/193429758-5320f849-797e-4525-81e2-6d84cdad6052.jpg" width="300" />
  <img src="https://user-images.githubusercontent.com/84316258/193429769-013a5614-33b5-4c80-ab9c-f1246ed0af8b.jpg" width="300" />
  
* Custom alert dialog

  <img src="https://user-images.githubusercontent.com/84316258/193429840-b38a967c-55e9-4172-a95a-a19537ee8ff5.jpg" width="300" />

* Khi click vào Deleted Note sẽ có Alert Dialog thông báo phải Recover mới được Edit Note.

  https://user-images.githubusercontent.com/84316258/193433810-a74dc44b-54a3-4b48-9130-04b78060413a.mp4


* Popup Menu để chọn sort theo id, title hoặc time.

  https://user-images.githubusercontent.com/84316258/193433863-87657986-6de8-441f-b596-c39b3bbaebd7.mp4

* Search theo Title. 

  https://user-images.githubusercontent.com/84316258/193433933-96807b1d-bc8c-44a0-a945-4f68bf3afe97.mp4

* Swipe Note để xoá, có thể Undo.

  https://user-images.githubusercontent.com/84316258/193433965-3e17f504-9415-4182-84d9-7630311356b2.mp4


* Swipe Deleted Note sang trái để xoá hẳn Note, sang phải để Recover Note.

  https://user-images.githubusercontent.com/84316258/193433980-452804bb-524e-4ed1-82d7-1a1351b68080.mp4

