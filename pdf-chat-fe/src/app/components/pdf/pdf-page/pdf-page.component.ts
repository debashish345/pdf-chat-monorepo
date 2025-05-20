import { CommonModule } from '@angular/common';
import { Component, ElementRef, inject, ViewChild } from '@angular/core';
import { environment } from '../../../../environments/environment';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { finalize } from 'rxjs';


interface UploadResult {
  success: boolean;
  message: string;
  url?: string; // Optional: URL of the uploaded file
}

@Component({
  selector: 'app-pdf-page',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './pdf-page.component.html',
  styleUrl: './pdf-page.component.css'
})
export class PdfPageComponent {

  private http = inject(HttpClient);

  @ViewChild('fileInput') fileInput!: ElementRef<HTMLInputElement>;
  selectedFile?: File;
  uploadError: string | null = null;
  uploadResult?: UploadResult | null;

  triggerFileInput(): void {
    this.fileInput.nativeElement.click();
  }

  onFileSelected(event: any): void {
    this.selectedFile = event.target.files[0] as File;
    console.log('Selected File:', this.selectedFile);
  }

  uploadFile() {
    console.log('Uploading file:', this.selectedFile);
    const formData = new FormData();
    formData.append('file', this.selectedFile!, this.selectedFile!.name);

    fetch(environment.fileUploadEndPoint, {
      method: 'POST',
      body: formData,
    })
    .then((response: any) => {
      if (response.ok) {
        this.uploadError = null;
      } else {
        return response.json().then((data: any) => {
          this.uploadError = data.message || 'Upload failed';
        }).catch(() => {
          this.uploadError = 'Upload failed';
        });
      }
    })
    .catch(error => {
      this.uploadError = error.message || 'Network error during upload';
    });
  }

  uploadFileWithPresignedUrl() {
    if (!this.selectedFile) {
      this.uploadResult = { success: false, message: 'Please select a file to upload.' };
      return;
    }
    const filename = this.selectedFile.name;
    this.getPresignedUrl(filename);
  }



  private getPresignedUrl(filename: string): void {
    const backendEndpoint = environment.fileUploadPresignedEndPointS3;
     this.http.post<{ presignedUrl: string }>(
      `${backendEndpoint}?filename=${encodeURIComponent(filename)}`,
      {}
    ).subscribe({
      next: (data: any) => {
        if (!data?.url) {
          console.log('No presigned URL received from the server.');
          return;
        }
        console.log('Presigned URL:', data.url);
        this.uploadFileToS3(data.url);
        const uploadCompleteEndPoint = environment.uploadCompleteEndPoint;
        this.http.get<void>(`${uploadCompleteEndPoint}?filename=${encodeURIComponent(filename)}`).subscribe({
          next: () => {
            console.log('Upload complete endpoint called successfully.');
          },
          error: (error) => {
            console.error('Error calling upload complete endpoint:', error);
          }
        });
      },
      error: (error) => {
        console.error('Error fetching presigned URL:', error);
      }
    });
  }



  uploadFileToS3(presignedUrl: string): void {
    if (!this.selectedFile) {
      this.uploadResult = { success: false, message: 'Please select a file to upload.' };
      return;
    }

    this.uploadResult = null;
    console.log('Uploading file to S3 with presigned URL:', presignedUrl);
    // Use HttpClient to upload to S3
    this.http.put(presignedUrl, this.selectedFile, {
      headers: new HttpHeaders({
        'Content-Type': this.selectedFile.type,
      }),
      observe: 'response',
    })
      .subscribe({
        next: (uploadResponse) => {
          if (uploadResponse.status >= 200 && uploadResponse.status < 300) {
            // Construct the *publicly accessible* URL.
            const publicUrl = presignedUrl.split("?")[0];
            this.uploadResult = { success: true, message: 'File uploaded successfully!', url: publicUrl };
          } else {
            this.uploadResult = { success: false, message: `File upload failed: ${uploadResponse.status}` };
          }
        },
        error: (error) => {
          console
        }
      });
  }

}
