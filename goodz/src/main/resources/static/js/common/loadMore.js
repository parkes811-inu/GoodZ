// 데이터 로드 함수
async function loadData(url) {
    const response = await fetch(url);
    if (!response.ok) {
        throw new Error('Network response was not ok');
    }
    return response.json();
}

// 카드 생성 함수
function createTaggedStyleCard(data) {
    return `
        <div class="col">
            <div class="card border-0">
                <div class="card-body">
                    <div class="card-text py-2">
                        <div class="d-flex justify-content-start column-gap-1">
                            <div class="profile-img">
                                <img src="${data.profileImg}" alt="프로필이미지" class="rounded-circle">
                            </div>
                            <p class="userId fw-bold m-0">${data.userId}</p>
                        </div>
                    </div>
                    <a href="#">
                        <img src="${data.postImg}" alt="게시글" class="rounded-4" style="width: 100%;">
                    </a>
                </div>
            </div>
        </div>
    `;
}

function createBrandProductCard(data) {
    return `
        <div class="col">
            <div class="card border-0">
                <div class="card-body">
                    <a href="#">
                        <img src="${data.postImg}" alt="게시글" class="rounded-4" style="width: 100%;">
                    </a>
                    <div class="card-text py-2">
                        <div class="d-flex justify-content-start column-gap-1">
                            <p class="userId fw-bold m-0">${data.productName}</p>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    `;
}

// 더보기 로드 함수
function loadMore(containerId, data, createCard, limit) {
    const container = document.getElementById(containerId);
    const currentItems = container.getElementsByClassName('col').length;
    const nextItems = data.slice(currentItems, currentItems + limit);
    nextItems.forEach(item => {
        container.innerHTML += createCard(item);
    });

    if (container.getElementsByClassName('col').length >= data.length) {
        document.getElementById(`loadMore${containerId.charAt(0).toUpperCase() + containerId.slice(1)}`).classList.add('hidden');
    }
}

// 초기 데이터 로드 및 더보기 버튼 관리
document.addEventListener('DOMContentLoaded', async function() {
    const initialLoadLimit = 4;

    try {
        const taggedStylesData = await loadData('/api/taggedStyles');
        const brandProductsData = await loadData('/api/brandProducts');

        loadMore('taggedStyles', taggedStylesData, createTaggedStyleCard, initialLoadLimit);
        loadMore('brandProducts', brandProductsData, createBrandProductCard, initialLoadLimit);

        document.getElementById('loadMoreTagged').addEventListener('click', () => {
            loadMore('taggedStyles', taggedStylesData, createTaggedStyleCard, initialLoadLimit);
        });

        document.getElementById('loadMoreBrand').addEventListener('click', () => {
            loadMore('brandProducts', brandProductsData, createBrandProductCard, initialLoadLimit);
        });

        if (taggedStylesData.length > initialLoadLimit) {
            document.getElementById('loadMoreTagged').classList.remove('hidden');
        }
        if (brandProductsData.length > initialLoadLimit) {
            document.getElementById('loadMoreBrand').classList.remove('hidden');
        }
    } catch (error) {
        console.error('Error loading data:', error);
    }
});
